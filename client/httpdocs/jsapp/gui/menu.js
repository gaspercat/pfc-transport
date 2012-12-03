gui_menu_count = 0;

function gui_menu(){
    this.id = gui_menu_count;
    
    // Positioning attributes
    this.left = 0;
    this.top = 0;
    this.width = 160;
    
    // Content attributes
    this.buttons = new Array();
    
    // Auxiliary attributes
    this.locked = false;
    
    // ********************************************************* //
    // ** PUBLIC :: GETTERS                                   ** //
    // ********************************************************* //
    
    this.getID = function(){
        return this.id;
    };
    
    this.getLeft = function(){
        return this.left;
    };
    
    this.getTop = function(){
        return this.top;
    };
    
    this.getWidth = function(){
        return this.width;
    };
    
    // ********************************************************* //
    // ** PUBLIC :: SETTERS                                   ** //
    // ********************************************************* //
    
    this.setLeft = function(left){
        this.left = left;
    };
    
    this.setTop = function(top){
        this.top = top;
    };
    
    this.setPosition = function(left, top){
        this.left = left;
        this.top = top;
    };
    
    // ********************************************************* //
    // ** PUBLIC :: ACTUATORS                                 ** //
    // ********************************************************* //
    
    this.addButton = function(id, name, slot){
        // Generate button
        var button = new Array();
        button['id'] = id;
        button['name'] = name;
        button['slot'] = slot;
        
        // Add button to buttons list
        this.buttons[this.buttons.length] = button;
    };
    
    this.delButton = function(id){
        for(var i=0;i<this.buttons.length;i++){
            if(this.buttons[i]['id'] == id){
                this.buttons.splice(i, 1);
            }
        }
    };
    
    // ********************************************************* //
    // ** PUBLIC :: DRAWING METHODS                           ** //
    // ********************************************************* //
    
    this.draw = function(){
        var self = this;
 
        // Lock menu to prevent it from vanishing because of the click event
        this.locked = true;
        
        // Draw menu
        var ret = document.createElement('table');
            ret.setAttribute('id', this.getElementID());
            ret.setAttribute('class', 'gui_menu');
            ret.style.left = this.left + 'px';
            ret.style.top = this.top + 'px';
            ret.style.width = this.width + 'px';
        
            // Draw menu buttons
            for(var i=0;i<this.buttons.length;i++){
                var domElem = this.drawElement(this.buttons[i]);
                ret.appendChild(domElem);
            }
        
        return ret;
    };
    
    // ********************************************************* //
    // ** PRIVATE :: DRAWING METHODS                          ** //
    // ********************************************************* //
    
    this.drawElement = function(elem){
        var self = this;
        
        var ret = document.createElement('tr');
           var td = document.createElement('td');
               td.setAttribute('class', 'gui_menu_button');
               td.appendChild(document.createTextNode(elem['name']));
               td.onclick = function(){self.signalClick(elem);};
           ret.appendChild(td);
        
        return ret;
    }
    
    // ********************************************************* //
    // ** PRIVATE :: SIGNALING METHODS                        ** //
    // ********************************************************* //

    this.signalClick = function(button){
        // Signal the tab sot
        if(button['slot']) button['slot']();
    }
    
    this.slotLoseFocus = function(){
        if(this.locked == true){
            this.locked = false;
            return;
        }
        
        var domElem = document.getElementById(this.getElementID());
        if(domElem) domElem.removeNode();
        
        //this.locked = true;
    };
    
    // ********************************************************* //
    // ** PRIVATE :: AUXILIARY METHODS                        ** //
    // ********************************************************* //
    
    this.getElementID = function(){
        return 'gui_menu_' + this.id;
    }
    
    // ********************************************************* //
    // ** CLASS CONSTRUCTOR                                   ** //
    // ********************************************************* //
    
    // Associate handlers to triggers
    var self = this;
    globalEvents.addClickSlot(function(){ self.slotLoseFocus(); });
}