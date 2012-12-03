function gui_field_numeric(){
    this.width = 120;
    this.height = 20;
    
    this.element = null;
    this.value = 0;
    
    // ********************************************************* //
    // ** PUBLIC :: GETTERS                                   ** //
    // ********************************************************* //
    
    this.getValue = function(){
        return this.value;
    };
    
    this.getWidth = function(){
        return this.width;
    };
    
    this.getHeight = function(){
        return this.height;
    };
    
    // ********************************************************* //
    // ** PUBLIC :: SETTERS                                   ** //
    // ********************************************************* //
    
    this.setValue = function(value){
        this.value = value;
    };
    
    this.setWidth = function(width){
        this.width = width;
    };
    
    this.setHeight = function(height){
        this.height = height;
    };
    
    this.setSize = function(width, height){
        this.width = width;
        this.height = height;
    };
    
    this.setFocus = function(){
        this.element.focus();
    };
    
    // ********************************************************* //
    // ** PUBLIC :: DRAWING METHODS                           ** //
    // ********************************************************* //
    
    this.draw = function(){
        var ret = document.createElement('input');
            ret.setAttribute('type', 'text');
            ret.style.height = this.height + 'px';
            ret.style.width = this.width + 'px';
            ret.style.border = '0px';
            ret.style.textAlign = 'right';
            ret.style.marginRight = '5px';
            ret.style.fontSize = '14px';
            ret.style.padding = '0px';
            ret.backgroundColor = '#FFF';
            ret.setAttribute('value', this.value);
            
            var self = this;
            ret.onkeydown = function(){
                var evt = arguments[0] || window.event;
                evt.preventDefault();
                
                // Get character and cursor position
                var chr = String.fromCharCode(evt.keyCode);
                var pos = ret.getCursorPosition();
                
                // If left arrow
                if(evt.keyCode == '37'){
                    if(pos == 0) return;
                    ret.setCursorPosition(pos-1);
                    return;
                    
                // If right arrow
                }else if(evt.keyCode == '39'){
                    if(pos == ret.value.length) return;
                    ret.setCursorPosition(pos+1);
                    return;
                    
                // If backspace
                }else if(evt.keyCode == '8'){
                    if(pos == 0) return;
                    if(pos == 1 && ret.value.charAt(1) == '.'){
                        ret.value = ret.value.splice(pos-1, 1, '0');
                    }else{
                        ret.value = ret.value.splice(pos-1, 1);
                    }
                    ret.setCursorPosition(pos-1);
                    return;
                
                // If decimal separator
                }else if(chr == '.' || evt.keyCode == 190){
                    var n = this.value.split('.').length;
                    if(n > 1) return;
                    ret.value = ret.value.splice(pos, 0, (pos == 0) ? '0.' : '.' );
                    self.value = ret.value.parseFloat();
                    ret.setCursorPosition(pos+1);
                    return;
                
                }else if(chr >= '0' && chr <= '9'){
                    ret.value = ret.value.splice(pos, 0, chr);
                    self.value = ret.value.parseFloat();
                    ret.setCursorPosition(pos+1);
                    return;
                }
            };
                    
        this.element = ret;
        return ret;
    };
}