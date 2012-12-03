var gui_tabs_count = 0;

function gui_tabs(){
    this.id = gui_tabs_count;
    
    // Positioning attributes
    this.left = 0;
    this.top = 0;
    this.width = 100;
    this.height = 30;
    
    // Content attributes
    this.ltabs = new Array();
    this.rtabs = new Array();
    this.clicked = null;
    this.selected = null;
    
    // Auxiliary atributes
    this.locked = false;
    
    gui_tabs_count++;
    
    // ********************************************************* //
    // ** PUBLIC :: GETTERS                                   ** //
    // ********************************************************* //
    
    this.getLeft = function(){
        return this.left;
    };
    
    this.getTop = function(){
        return this.top;
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
    
    // ********************************************************* //
    // ** PUBLIC :: CONTENT MANAGEMENT                        ** //
    // ********************************************************* //
    
    this.addTabLeft = function(id, name, slot){
        // Create new tab
        var ntab = new Array();
        ntab['id'] = id;
        ntab['name'] = name;
        ntab['slot'] = slot;
        
        // Add tab to tabs list
        this.ltabs[this.ltabs.length] = ntab;
    };
    
    this.addTabRight = function(id, name, slot){
        // Create new tab
        var ntab = new Array();
        ntab['id'] = id;
        ntab['name'] = name;
        ntab['slot'] = slot;
        
        // Add tab to tabs list
        this.rtabs[this.rtabs.length] = ntab;
    };
    
    this.delTab = function(id){
        var i;
        
        for(i=0;i<this.ltabs.length;i++){
            if(this.ltabs[i]['id'] == id){
                if(this.selected == this.ltabs[i]) this.selected = null;
                if(this.clicked == this.ltabs[i]) this.clicked = null;
                this.ltabs.splice(i, 1);
                return;
            }
        }
        
        for(i=0;i<this.rtabs.length;i++){
            if(this.rtabs[i]['id'] == id){
                if(this.selected == this.rtabs[i]) this.selected = null;
                if(this.clicked == this.rtabs[i]) this.clicked = null;
                this.rtabs.splice(i, 1);
                return;
            }
        }
    };
    
    this.selTab = function(id){
        var i;
        
        // Mark tab as selected
        for(i=0;i<this.ltabs.length;i++){
            if(this.ltabs[i]['id'] == id){
                this.selected = this.ltabs[i];
                break;
            }
        }
        
        // Mark tab as selected
        for(i=0;i<this.rtabs.length;i++){
            if(this.rtabs[i]['id'] == id){
                this.selected = this.rtabs[i];
                break;
            }
        }
        
        // Redraw element
        var domElem = document.getElementById(this.getElementID());
        if(domElem) domElem.replaceNode(this.draw());
    };
    
    // ********************************************************* //
    // ** PUBLIC :: DRAWING METHODS                           ** //
    // ********************************************************* //

    this.draw = function(width, height){
        var i;
        
        var ret = document.createElement('div');
            ret.setAttribute('id', this.getElementID());
            ret.style.position = 'absolute';
            ret.style.left = this.left + 'px';
            ret.style.top = this.top + 'px';
            ret.style.width = this.width + 'px';
            ret.style.height = this.height + 'px';
            
            // Add tabs
            var tabs = document.createElement('table');
                tabs.style.width = '100%';
                tabs.style.height = '100%';
                tabs.style.border = '0px';
                tabs.style.borderCollapse = 'collapse';
                
                var tr = document.createElement('tr');
                
                    // Add left tabs
                    for(i=0;i<this.ltabs.length;i++){
                        tr.appendChild(this.drawElement(this.ltabs[i]));
                    }
                
                    // Add dummy spacing
                    var tab = document.createElement('td');
                    tr.appendChild(tab);

                    // Add right tabs
                    for(i=0;i<this.rtabs.length;i++){
                        tr.appendChild(this.drawElement(this.rtabs[i]));
                    }
                
                tabs.appendChild(tr)
                
            ret.appendChild(tabs);
                
        return ret;
    };
    
    // ********************************************************* //
    // ** PRIVATE :: DRAWING METHODS                          ** //
    // ********************************************************* //
    
    this.drawElement = function(tab){
        var self = this;
        
        var ret = document.createElement('td');
            if((this.clicked == null && this.selected == tab) || this.clicked == tab){
                ret.setAttribute('class', 'gui_tabs_tab gui_tabs_selected');
            }else{
                ret.setAttribute('class', 'gui_tabs_tab gui_tabs_uselected');
            }
          
            ret.appendChild(document.createTextNode(tab['name']));
            ret.onclick = function(){self.signalClick(tab);};

        return ret;
    };
    
    // ********************************************************* //
    // ** PRIVATE :: SIGNALING METHODS                        ** //
    // ********************************************************* //

    this.signalClick = function(tab){
        this.locked = true;
        
        // Set clicked tab
        this.clicked = tab;
        
        // Signal the tab sot
        if(tab['slot']) tab['slot']();
        
        // Redraw tab
        var domElem = document.getElementById(this.getElementID());
        if(domElem) domElem.replaceNode(this.draw());
    };
    
    // ********************************************************* //
    // ** PRIVATE :: SIGNAL RECEIVING METHODS                 ** //
    // ********************************************************* //

    this.slotLoseFocus = function(){
        if(this.locked == true){
            this.locked = false;
            return;
        }
        
        // Deselect selected element
        this.clicked = null;
        
        // Redraw tab
        var domElem = document.getElementById(this.getElementID());
        if(domElem) domElem.replaceNode(this.draw());
    };

    // ********************************************************* //
    // ** PRIVATE :: AUXILIARY METHODS                        ** //
    // ********************************************************* //
    
    this.getElementID = function(){
        return 'gui_tabs_' + this.id;
    };
    
    // ********************************************************* //
    // ** CLASS CONSTRUCTOR                                   ** //
    // ********************************************************* //
    
    // Set click slot global click method
    var self = this;
    globalEvents.addClickSlot(function(){self.slotLoseFocus();});
}