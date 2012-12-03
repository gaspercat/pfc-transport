function gui_field_text(){
    this.width = 120;
    this.height = 20;
    
    this.element = null;
    this.value = 0;
    
    // ********************************************************* //
    // ** GETTERS                                             ** //
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
    // ** GETTERS                                             ** //
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
    // ** DRAWING FUNCTIONS                                   ** //
    // ********************************************************* //
    
    this.draw = function(){
        var ret = document.createElement('input');
            ret.setAttribute('type', 'text');
            ret.style.height = this.height + 'px';
            ret.style.width = this.width + 'px';
            ret.style.border = '0px';
            ret.style.textAlign = 'left';
            ret.style.marginRight = '5px';
            ret.style.fontSize = '14px';
            ret.style.padding = '0px';
            ret.backgroundColor = '#FFF';
            ret.setAttribute('value', this.value);
            
            var self = this;
            ret.onkeyup = function(){
                self.value = ret.value;
            };
                    
        this.element = ret;
        return ret;
    };
}