var gui_list_count = 0;

function gui_list(name){
    this.id = gui_list_count;
    gui_list_count++;
    
    this.name = name;
    
    // Positioning attributes
    this.left = 0;
    this.top = 0;
    this.width = 200;
    this.height = 500;
    
    // Customization attributes
    this.editable = false;
    
    // Content attributes
    this.list = new Array();
    this.selected = null;
    
    // Slots for list signals
    this.slotclk = null;
    this.slotdbclk = null;
    this.slotcreate = null;
    this.slotremove = null;
    this.slotedit = null;
    
    // ********************************************************* //
    // ** PUBLIC :: GETTERS                                   ** //
    // ********************************************************* //
    
    this.getID = function(){
        return this.id;
    };
    
    this.getDomID = function(){
        return this.getElementID();
    };
    
    this.getWidth = function(){
        return this.width;
    };
    
    this.getHeight = function(){
        return this.height;
    };
    
    this.getLeft = function(){
        return this.left;
    };
    
    this.getTop = function(){
        return this.top;
    };
    
    this.getSelectedElement = function(){
        if(this.selected == null) return null;
        return this.selected['object'];
    };
    
    // ********************************************************* //
    // ** PUBLIC :: SETTERS                                   ** //
    // ********************************************************* //
    
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
    
    this.setEditable = function(editable){
        this.editable = editable;
    };
    
    // ********************************************************* //
    // ** PUBLIC :: SIGNALS MANAGEMENT                        ** //
    // ********************************************************* //
    
    this.setClickSlot = function(funct){
        this.slotclk = funct;
    };
    
    this.setDblClickSlot = function(funct){
        this.slotdbclk = funct;
    };
    
    this.setCreateSlot = function(funct){
        this.slotcreate = funct;
    };
    
    this.setRemoveSlot = function(funct){
        this.slotremove = funct;
    };
    
    this.setEditSlot = function(funct){
        this.slotedit = funct;
    };
    
    // ********************************************************* //
    // ** PUBLIC :: CONTENT MANAGEMENT                        ** //
    // ********************************************************* //
    
    /**
     * Add an element to the list, with a given ID, name, and two objects that
     * generate the content of the expanded list element and the independent
     * content
     */
    this.addElement = function(id, name, object){
        // Create new element
        var elem = new Array();
        elem['id'] = id;
        elem['name'] = name;
        elem['object'] = object;
        
        // Intert element ordered by ID
        for(i=0;i<this.list.length;i++){
            if(this.list[i]['id'] > id){
                this.list.splice(i, 0, elem);
                break;
            }
        }
        
        // If last element has a lower id, insert at the end
        if(this.list.length == 0 || this.list[this.list.length-1]['id'] <= elem['id']){
            this.list[this.list.length] = elem;
        }
        
        // Update list dom element if present
        var domElem = document.getElementById(this.getElementID());
        if(domElem) domElem.replaceNode(this.draw());
    };

    /**
     * Remove the element with the given 'id' from the list
     */
    this.delElement = function(id){
        for(var i=0;i<this.list.length;i++){
            if(this.list[i]['id'] == id){
                var domID = this.getChildID(this.list[i]['id']);
                
                // Remove element from list
                var domElem = document.getElementById(domID);
                if(domElem) domElem.removeNode();
                
                // If element was selected, remove summary
                if(this.list[i] == this.selected){
                    this.selected = null;
                    domElem = document.getElementById(domID + '_exp');
                    if(domElem) domElem.removeNode();
                }
                
                // Remove element from list
                this.list.splice(i, 1);
                
                break;
            }
        }
    };
    
    this.updElement = function(id, name, object){
        var domElem, domID;
        
        for(var i=0;i<this.list.length;i++){
            if(this.list[i]['id'] == id){
                // Update element
                this.list[i]['id'] = id;
                this.list[i]['name'] = name;
                this.list[i]['object'] = object;
                
                // Update dom element if present
                domID = this.getChildID(this.list[i]['id']);
                domElem = document.getElementById(domID);
                if(domElem){
                    domElem.replaceNode(this.drawElement(this.list[i]));
                    
                    // If element is selected, update summary
                    if(this.list[i] == this.selected){
                        domElem = document.getElementById(domID + '_exp');
                        domElem.replaceNode(this.drawSummary(this.selected));
                    }
                }
                
                break;
            }
        }
    }
    
    // ********************************************************* //
    // ** PUBLIC :: DRAWING METHODS                           ** //
    // ********************************************************* //
    
    this.draw = function(){
        var self = this;
        var telem, teleme;
        
        var box = document.createElement('div');
            box.setAttribute('id', this.getElementID());
            box.setAttribute('class', 'gui_list');
            box.style.left = this.left;
            box.style.top = this.top;
            box.style.width = (this.width == -1) ? '100%' : this.width + 'px';
            box.style.height = (this.height == -1) ? '100%' : this.height + 'px';
           
            // Add table for the listing
            var list = document.createElement('table');
                list.setAttribute('class', 'gui_list');

                // Add title
                var thead = document.createElement('thead');
                    thead.style.display = 'block';
                    thead.style.overflow = 'auto';
                    thead.style.width = '100%';
                    telem = document.createElement('tr');
                        telem.setAttribute('class', 'gui_list_header');
                        telem.style.display = 'block';
                        teleme = document.createElement('td');
                            teleme.appendChild(document.createTextNode(this.name));
                        telem.appendChild(teleme);
                    thead.appendChild(telem);
                list.appendChild(thead);

                // Add list elements
                var tbody = document.createElement('tbody');
                    tbody.style.display = 'block';
                    tbody.style.overflow = 'auto';
                    tbody.style.width = (this.width - 2) + 'px';
                    if(this.editable) tbody.style.height = (this.height - 25*2 - 2) + 'px';
                    else tbody.style.height = (this.height - 25 - 2) + 'px';
                    tbody.style.border = '1px solid #FFF';
                    for(var i=0;i<this.list.length;i++){
                        telem = this.drawElement(this.list[i]);
                        tbody.appendChild(telem);
                        // If element is selected, draw summary
                        if(this.list[i] == this.selected){
                            tbody.appendChild(this.drawSummary(this.selected));
                        }
                    }
                    
                    // Add empty final element
                    telem = document.createElement('tr');
                        teleme = document.createElement('td');
                        telem.appendChild(teleme);
                    tbody.appendChild(telem);
                list.appendChild(tbody);

                if(this.editable){
                    // Add edition buttons
                    var tfoot = document.createElement('tfoot');
                        tfoot.style.display = 'block';
                        tfoot.style.overflow = 'auto';
                        tfoot.style.width = '100%';
                        telem = document.createElement('tr');
                            telem.setAttribute('class', 'gui_list_footer')
                            telem.style.display = 'block';
                            // White spacing
                            teleme = document.createElement('td');
                                teleme.style.width = (this.width - 25*3) + 'px';
                            telem.appendChild(teleme);
                            // Add button
                            teleme = document.createElement('td');
                                teleme.setAttribute('class', 'bt_add');
                                teleme.onclick = function(){ self.signalCreate(); };
                            telem.appendChild(teleme);
                            // Remove button
                            teleme = document.createElement('td');
                                teleme.setAttribute('class', 'bt_del');
                                teleme.onclick = function(){ self.signalRemove(); };
                            telem.appendChild(teleme);
                            // Edit button
                            teleme = document.createElement('td');
                                teleme.setAttribute('class', 'bt_upd');
                                teleme.onclick = function(){ self.signalEdit(); };
                            telem.appendChild(teleme);
                        tfoot.appendChild(telem);
                    list.appendChild(tfoot);
                }

            box.appendChild(list);
              
        return box;
    };
    
    // ********************************************************* //
    // ** PRIVATE :: DRAWING METHODS                          ** //
    // ********************************************************* //
    
    this.drawElement = function(elem){
        var self = this;
        
        var ret = document.createElement('tr');
            ret.setAttribute('id', this.getChildID(elem['id']));
            ret.setAttribute('class', (elem == this.selected) ? 'gui_list_selected' : 'gui_list_uselected');
            ret.style.display = 'block';
            ret.style.width = '100%';
            
            var row = document.createElement('td');
                row.style.height = '20px';
                row.style.paddingLeft = '5px';
                row.appendChild(document.createTextNode(elem['name']));
            ret.appendChild(row);
           
            ret.onclick = function(){
                if(elem == self.selected) return;
                
                // Unselect previously selected list element
                if(self.selected != null){
                    // Set list element to not selected
                    var domElem = document.getElementById(self.getChildID(self.selected['id']));
                    domElem.setAttribute('class', 'gui_list_uselected');

                    // Remove previously selected element summary
                    var telemexp = document.getElementById(self.getChildID(self.selected['id']) + '_exp');
                    if(telemexp) telemexp.removeNode();
                }
                
                // Add element summary to the list
                appendAfter(ret, self.drawSummary(elem));
                
                // Mark element as selected
                ret.setAttribute('class', 'gui_list_selected');
                self.selected = elem;
                
                // Trigger click event
                self.signalClick(ret);
            };

            ret.ondblclick = function(){
                // Trigger double click event
                self.signalDblClick(ret);
            };
            
        return ret;
    };
    
    this.drawSummary = function(elem){
        var ret = document.createElement('tr');
            ret.setAttribute('id', this.getChildID(elem['id']) + '_exp');
            ret.setAttribute('class', 'gui_list_summary');
            var sum = document.createElement('td');
                sum.setAttribute('height', '1px');
                sum.appendChild(elem['object'].drawSummary());
            ret.appendChild(sum);
        
        return ret;
    };
    
    // ********************************************************* //
    // ** PRIVATE :: SIGNALING METHODS                        ** //
    // ********************************************************* //
    
    this.signalClick = function(domElem){
        var elem = this.getElementByDom(domElem);
        if(this.slotclk != null) this.slotclk(domElem, elem);
    };
    
    this.signalDblClick = function(domElem){
        var elem = this.getElementByDom(domElem);
        if(this.slotdbclk != null) this.slotdbclk(domElem, elem);
    };
    
    this.signalCreate = function(){
        if(this.slotcreate != null) this.slotcreate();
    };
    
    this.signalRemove = function(){
        if(this.slotremove == null || this.selected == null) return;
        
        var domElem = document.getElementById(this.getChildID(this.selected['id']));
        var elem = this.selected['object'];
        this.slotremove(domElem, elem);
    };
    
    this.signalEdit = function(){
        if(this.slotedit == null || this.selected == null) return;
        
        var domElem = document.getElementById(this.getChildID(this.selected['id']));
        var elem = this.selected['object'];
        this.slotedit(domElem, elem);
    };
    
    // ********************************************************* //
    // ** PRIVATE :: AUXILIARY METHODS                        ** //
    // ********************************************************* //
    
    this.getElementID = function(){
        return 'gui_list_' + this.id;
    };
    
    this.getChildID = function(id){
        return 'gui_list_' + this.id + '_' + id;
    };
    
    this.getElementByDom = function(domElem){
        // Get element ID
        var id = domElem.getAttribute('id');
        id = parseInt(id.split('_')[3]);
        // Get element by ID
        for(var i=0;i<this.list.length;i++){
            if(this.list[i]['id'] == id){
                return this.list[i]['object'];
            }
        }
        
        return null;
    };
}