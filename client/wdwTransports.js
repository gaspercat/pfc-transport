function wdwTransports(){
    // GUI elements
    this.list = null;
    this.boxEdit = null;
    this.calendar = null;
    this.schedule = null;
    
    // Window DOM element
    this.domElement = null;
    
    // ********************************************************* //
    // ** PUBLIC :: DRAWING METHODS                           ** //
    // ********************************************************* //
    
    this.draw = function(){
        var ret = document.createElement('div');
            ret.setAttribute('id', this.getElementID());
            ret.setAttribute('class', 'window_content');
            ret.style.display = 'block';
            ret.style.position = 'absolute';
            ret.style.top = '110px';
            ret.style.bottom = '0px';
            ret.style.left = '0px';
            ret.style.right = '0px';
            
            // Draw list
            ret.appendChild(this.list.draw());
            
            // If list element selected, draw calendar & schedule
            if(this.list.getSelectedElement() != null){
                ret.appendChild(this.calendar.draw());
                ret.appendChild(this.schedule.draw());
            }
            
        this.domElement = ret;
        return ret;
    };
    
    this.clean = function(){
        // Remove DOM element
        var domElem = document.getElementById(this.getElementID());
        domElem.removeNode();
        this.domElement = null;
    };
    
    // ********************************************************* //
    // ** PRIVATE :: TRIGGERS                                 ** //
    // ********************************************************* //
    
    this.slotRepoAdd = function(json){
        var driver = new gui_wrapper_vignette(newTransportFromJson(json));
        
        // Add driver to list
        this.list.addElement(json['id'], json['name'], driver);
    };
    
    this.slotRepoDel = function(json){
        // Remove driver from GUI list
        this.list.delElement(json['id']);
    };
    
    this.slotRepoUpd = function(json){
        var driver = new gui_wrapper_vignette(newDriverFromJson(json));
        
        // Update driver at GUI list
        this.list.updElement(json['id'], json['name'], driver);
    }
    
    this.slotListDblClick = function(trigger, element){
        if(element == null) return;
        
        // Prepare new edition box
        this.boxEdit = element;
        element.setPosition(200, trigger.getPositionY() - this.domElement.getPositionY() - 28);
        element.setSize(275, 200);
        
        // Connect vignette signals to slots
        var self = this;
        this.boxEdit.setUpdateSlot(function(element){ self.slotVignetteUpdate(element); });
        
        // Append edition box to drivers window
        this.domElement.appendChild(this.boxEdit.draw());
        
        // Give focus
        this.boxEdit.setFocus();
    };
    
    this.slotListCreate = function(){
        
    };
    
    this.slotListRemove = function(trigger, element){
        // Remove driver from GUI list
        this.list.delElement(element.getID());
        
        // Inform the repository for sync with server
        repository.removeElement(repository.repoDrivers, element.getID());
    };
    
    this.slotListEdit = function(trigger, element){
        element.lock();
        this.slotDblClick(trigger, element);
    };
    
    this.slotVignetteUpdate = function(element){
        // Refresh element at the repository
        repository.editElement(repository.repoDrivers, element);
    };
    
    // ********************************************************* //
    // ** PRIVATE AUXILIARY METHODS                           ** //
    // ********************************************************* //
    
    this.getElementID = function(){
        return 'window_transports';
    };
    
    this.generateList = function(){
        this.list = new gui_list('Transports');
        
        // Define list size & position
        this.list.setPosition(10, 10);
        this.list.setSize(200, 430);
        this.list.setEditable(true);
        
        // Set list's double click trigger
        var self = this;
        this.list.setDblClickSlot(function(trig, elem){self.slotListDblClick(trig, elem);});
    };
    
    // ********************************************************* //
    // ** CLASS CONSTRUCTOR                                   ** //
    // ********************************************************* //
    
    // Generate GUI elements
    this.generateList();
    
    // Connect slots to the repository triggers
    var self = this;
    repository.addAdditionSlot(repository.repoDrivers, function(json){self.slotRepoAdd(json);});
    repository.addDeletionSlot(repository.repoDrivers, function(json){self.slotRepoDel(json);});
    repository.addUpdateSlot(repository.repoDrivers, function(json){self.slotRepoUpd(json);});
    
    // Connect slots to the list triggers
    this.list.setClickSlot(function(trig, elem){self.slotListClick(trig, elem);});
    this.list.setDblClickSlot(function(trig, elem){self.slotListDblClick(trig, elem);});
    this.list.setCreateSlot(function(){self.slotListCreate();});
    this.list.setRemoveSlot(function(trigger, element){self.slotListRemove(trigger, element);});
    this.list.setEditSlot(function(trigger, element){self.slotListEdit(trigger, element);});
    
    // Start syncronization of transports
    repository.activateRepository(repository.repoTransports);
}


