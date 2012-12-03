// ********************************************************* //
// ** DYNAMIC CLIENT-SERVER COMMUNICATIONS (AJAX)         ** //
// ********************************************************* //

function requestAjax(url, post_pars, response_funct){
    var xmlHttp;

    //Create AJAX Object
    try{
        xmlHttp = new XMLHttpRequest();
    }catch(e){
        try{
            xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
        }catch(e){
            try{
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            }catch(e){
                alert("Your browser does not support AJAX!");
                return false;
            }
        }
    }

    //Prepare catch function
    xmlHttp.onreadystatechange = function(){
        if(xmlHttp.readyState == 4){
            response_funct(xmlHttp.responseText);
        }
    }

    //Open connection
    xmlHttp.open("POST", url, true);
    xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlHttp.setRequestHeader("Content-length", post_pars.length);
    xmlHttp.setRequestHeader("Connection", "close");

    //Send request
    xmlHttp.send(post_pars);
}

// ********************************************************* //
// ** JSON encoding / decoding                            ** //
// ********************************************************* //

function encodeJson(arr) {
    var is_list = (Object.prototype.toString.apply(arr) === '[object Array]');
    var parts = [];

    for(var key in arr) {
    	var value = arr[key];
        
        // If a function, jump field
        if(typeof value == "function") continue;
        
        if(typeof value == "object") {
            //Custom handling for arrays
            if(is_list) parts.push(encodeJson(value));
            else parts.push('"' + key + '":' + encodeJson(value));
        } else {
            var str = "";
            if(!is_list) str = '"' + key + '":';

            //Custom handling for multiple data types
            if(typeof value == "number") str += value;    // Number
            else if(value === false) str += 'false';      // Boolean FALSE
            else if(value === true) str += 'true';        // Boolean TRUE
            else str += '"' + value + '"';                // String

            parts.push(str);
        }
    }
    
    var json = parts.join(",");
    
    if(is_list) return '[' + json + ']';                  // Return numerical JSON
    return '{' + json + '}';                              // Return associative JSON
}

var decodeJson = function () {
    var escapee = {'"':'"', '\\':'\\', '/':'/', b:'\b', f:'\f', n:'\n', r:'\r', t:'\t'};
    var at, ch, text;

    var error = function(m){
        throw {name:'SyntaxError', message:m, at:at, text:text};
    };

    var next = function (c) {
        if (c && c !== ch) error("Expected '" + c + "' instead of '" + ch + "'");
        ch = text.charAt(at);
        at += 1;
        return ch;
    };

    var number = function () {
        var number, string = '';

        if(ch === '-'){
            string = '-';
            next('-');
        }
        
        while(ch >= '0' && ch <= '9'){
            string += ch;
            next();
        }
        
        if(ch === '.'){
            string += '.';
            while (next() && ch >= '0' && ch <= '9') string += ch;
        }
        
        if(ch === 'e' || ch === 'E'){
            string += ch;
            next();
            if (ch === '-' || ch === '+') {
                string += ch;
                next();
            }
            while (ch >= '0' && ch <= '9') {
                string += ch;
                next();
            }
        }
        
        number = +string;
        if (!isFinite(number)) error("Bad number");
        else return number;
    };

    var string = function(){
        var hex, i, string = '', uffff;

        if (ch === '"') {
            while (next()) {
                if (ch === '"') {
                    next();
                    return string;
                } else if (ch === '\\') {
                    next();
                    if (ch === 'u') {
                        uffff = 0;
                        for (i = 0; i < 4; i += 1) {
                            hex = parseInt(next(), 16);
                            if (!isFinite(hex)) break;
                            uffff = uffff * 16 + hex;
                        }
                        string += String.fromCharCode(uffff);
                    } else if (typeof escapee[ch] === 'string') {
                        string += escapee[ch];
                    } else {
                        break;
                    }
                } else {
                    string += ch;
                }
            }
        }
        error("Bad string");
    };

    var white = function(){
        while (ch && ch <= ' ') next();
    };

    var word = function(){
        switch (ch) {
            case 't':next('t');next('r');next('u');next('e');return true;
            case 'f':next('f');next('a');next('l');next('s');next('e');return false;
            case 'n':next('n');next('u');next('l');next('l');return null;
        }
        error("Unexpected '" + ch + "'");
    };

    var array = function(){
        var array = [];

        if(ch === '['){
            next('[');
            white();
            if(ch === ']'){
                next(']');
                return array; // empty array
            }
            while(ch){
                array.push(value());
                white();
                if(ch === ']'){
                    next(']');
                    return array;
                }
                next(',');
                white();
            }
        }
        error("Bad array");
    };

    var object = function(){
        var key, object = {};

        if (ch === '{') {
            next('{');
            white();
            if(ch === '}'){
                next('}');
                return object; // empty object
            }
            while(ch){
                key = string();
                white();
                next(':');
                if (Object.hasOwnProperty.call(object, key)) error('Duplicate key "' + key + '"');
                object[key] = value();
                white();
                if(ch === '}'){
                    next('}');
                    return object;
                }
                next(',');
                white();
            }
        }
        error("Bad object");
    };

    var value = function () {
        white();
        switch (ch){
            case '{':return object();
            case '[':return array();
            case '"':return string();
            case '-':return number();
            default:return ch >= '0' && ch <= '9' ? number() : word();
        }
    };

    return function (source, reviver) {
        var result;

        text = source;
        at = 0;
        ch = ' ';
        result = value();
        white();
        
        if(ch) error("Syntax error");

        return (typeof reviver === 'function') ?
            (function walk(holder, key) {
                var k, v, value = holder[key];
                if (value && typeof value === 'object') {
                    for (k in value) {
                        if (Object.prototype.hasOwnProperty.call(value, k)) {
                            v = walk(value, k);
                            if (v !== undefined) value[k] = v;
                            else delete value[k];
                        }
                    }
                }
                return reviver.call(holder, key, value);
            }({'': result}, ''))
          :
            result;
    };
}();

// ********************************************************* //
// ** AUXILIARY FUNCTIONS                             ** //
// ********************************************************* //

function getMouseX(evt){
    return evt.clientX - document.body.getBoundingClientRect().left
}

function getMouseY(evt){
    return evt.clientY - document.body.getBoundingClientRect().top;
}

// ********************************************************* //
// ** AUXILIARY DOM FUNCTIONS                             ** //
// ********************************************************* //

function appendBefore(obj, node){
    obj.parentNode.insertBefore(node, obj);
}

function appendAfter(obj, node){
    if (obj.nextSibling){
        obj.parentNode.insertBefore(node, obj.nextSibling);
    }else{
        obj.parentNode.appendChild(node);
    }
}

// ********************************************************* //
// ** OBJECT CLASS ENRICHMENT                             ** //
// ********************************************************* //

Object.prototype.replaceNode = function(nnode){
    var onode = this;
    appendAfter(onode, nnode);
    onode.parentNode.removeChild(onode);
};

Object.prototype.removeNode = function(){
    this.parentNode.removeChild(this);
}

Object.prototype.getPositionX = function(){
	var ret = 0;
	
	var elem = this;
	while(elem != null){
		ret += elem.offsetLeft;
		elem = elem.offsetParent;
	}
	return ret;
};

Object.prototype.getPositionY = function(){
	var ret = 0;
	
	var elem = this;
	while(elem != null){
		ret += elem.offsetTop;
		elem = elem.offsetParent;
	}
	return ret;
};

Object.prototype.setCursorPosition = function(pos){
    if(this.createTextRange){
        var textRange = this.createTextRange();
        textRange.collapse(true);
        textRange.moveEnd(pos);
        textRange.moveStart(pos);
        textRange.select();
        return true;
    }else if(this.setSelectionRange){
        this.setSelectionRange(pos,pos);
        return true;
    }

    return false;
}

Object.prototype.getCursorPosition = function() {

  // Initialize
  var iCaretPos = 0;

  // IE Support
  if(document.selection){
    this.focus();
    var oSel = document.selection.createRange();
    oSel.moveStart ('character', -this.value.length);
    iCaretPos = oSel.text.length;
  // Firefox support
  }else if (this.selectionStart || this.selectionStart == '0'){
    iCaretPos = this.selectionStart;
  }

  return (iCaretPos);
}



// ********************************************************* //
// ** ARRAY CLASS ENRICHMENT                              ** //
// ********************************************************* //

Array.prototype.remove = function(elem){
    for(var i=0;i<this.length;i++){
        if(this[i] == elem){
            this.splice(i, 1);
            return elem;
        }
    }
    
    return null;
};

// ********************************************************* //
// ** STRING CLASS ENRICHMENT                             ** //
// ********************************************************* //

String.prototype.parseInt = function(){
    var val = 0;
    
    for(var i=0;i<this.length;i++){
        if(this.charAt(i) < '0' || this.charAt(i) > '9') return val;
        val = val * 10 + (this.charAt(i) - '0');
    }
    
    return val;
}

String.prototype.parseFloat = function(){
    var mod = 1, val = 0, i;
    
    for(i=0;i<this.length;i++){
        if(this.charAt(i) == '.') break;
        if(this.charAt(i) < '0' || this.charAt(i) > '9') return val;
        val = val * 10 + (this.charAt(i) - '0');
    }
    
    for(i=i;i<this.length;i++){
        mod = mod * 10;
        if(this.charAt(i) < '0' || this.charAt(i) > '9') return val;
        val = val + (this.charAt(i) - '0') / mod;
    }
    
    return val;
    
}

String.prototype.splice = function(i, n, e){
    var start = this.substr(0, i);
    var end = this.substr(i+n, this.length - i - n);
    
    if(e){
        return start + e + end;
    }else{
        return start + end;
    }
}