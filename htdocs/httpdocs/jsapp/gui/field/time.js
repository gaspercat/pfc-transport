function gui_field_time(){
    this.width = 120;
    this.height = 20;
    
    // Content attributes
    this.value = new TypeTime(0, 0);
    
    // Slots for list signals
    this.slotchange = null;
    
    // Edition DOM elements
    this.element = null;
    
    // ********************************************************* //
    // ** PUBLIC :: GETTERS                                   ** //
    // ********************************************************* //
    
    this.getValue = function(){
        return this.value;
    };
    
    this.getTextValue = function(){
        return this.value.toString();
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
    // ** PUBLIC :: SIGNALS MANAGEMENT                        ** //
    // ********************************************************* //
    
    this.setChangeSlot = function(funct){
        this.slotchange = funct;
    }
    
    // ********************************************************* //
    // ** PUBLIC :: DRAWING METHODS                           ** //
    // ********************************************************* //
    
    this.draw = function(){
        var ret = document.createElement('input');
            ret.setAttribute('type', 'text');
            ret.style.height = this.height + 'px';
            ret.style.width = this.width + 'px';
            ret.style.border = '0px';
            ret.style.textAlign = 'center';
            ret.style.marginRight = '5px';
            ret.style.fontSize = '14px';
            ret.style.padding = '0px';
            ret.backgroundColor = '#FFF';
            ret.setAttribute('value', this.value.toString());
            
            var self = this;
            
            ret.onmouseup = function(){
                var pos = ret.getCursorPosition();
                if(pos == 2) ret.setCursorPosition(3);
            }
            
            ret.onkeydown = function(){
                var evt = arguments[0] || window.event;
                evt.preventDefault();
                
                // Get character and cursor position
                var chr = String.fromCharCode(evt.keyCode);
                var pos = ret.getCursorPosition();
                
                // If left arrow
                if(evt.keyCode == '37'){
                    if(pos == 0) return;
                    if(pos == 3) pos--;
                    ret.setCursorPosition(pos-1);
                    return;
                    
                // If right arrow
                }else if(evt.keyCode == '39'){
                    if(pos == ret.value.length) return;
                    if(pos == 1) pos++;
                    ret.setCursorPosition(pos+1);
                    return;
                }
                
                // If non-numeric key, exit
                if(chr < '0' || chr > '9') return;
                
                // Replace the character at the string
                ret.value = ret.value.splice(pos, 1, chr);
                
                // Divide into minutes and hours
                var parts = ret.value.split(':');
                var hour = parts[0].parseInt();
                var minute = parts[1].parseInt();
                
                // Check max. values
                if(hour > 24) hour = 24;
                if(minute > 59) minute = 59;
                if(hour == 24) minute = 0;

                // Set value
                this.value = new TypeTime(hour, minute);
                ret.value = this.value.toString();
                
                // Move cursor
                if(pos == 1) pos++;
                ret.setCursorPosition(pos+1);
                
                // Signal change on value
                self.signalChange();
            };
                    
        this.element = ret;
        return ret;
    };
    
    // ********************************************************* //
    // ** PRIVATE :: SIGNALING METHODS                        ** //
    // ********************************************************* //
    
    this.signalChange = function(){
        if(this.slotchange) this.slotchange();
    }
}