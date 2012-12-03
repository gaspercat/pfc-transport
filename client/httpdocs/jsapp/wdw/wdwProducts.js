function wdwProducts(){
    // GUI elements
    this.list = null;
    this.boxEdit = null;
    
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
            
            ret.appendChild(this.list.draw());
            
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
        var product = new gui_wrapper_vignette(newProductFromJson(json));
        
        // Add product to GUI list
        this.list.addElement(json['id'], json['name'], product);
    };
    
    this.slotRepoDel = function(json){
        // Remove product from GUI list
        this.list.delElement(json['id']);
    };
    
    this.slotRepoUpd = function(json){
        var product = new gui_wrapper_vignette(newProductFromJson(json));

        // Update product at GUI list
        this.list.updElement(json['id'], json['name'], product);
    }
    
    this.slotDblClick = function(trigger, element){
        if(element == null) return;
        
        // Prepare new edition box
        this.boxEdit = element;
        element.setPosition(200, trigger.getPositionY() - this.domElement.getPositionY() - 28);
        element.setSize(255, 170);
        
        // Connect vignette signals to slots
        var self = this;
        this.boxEdit.setUpdateSlot(function(element){ self.slotVignetteUpdate(element); });
        
        // Append edition box to products window
        this.domElement.appendChild(this.boxEdit.draw());
        
        // Give focus
        this.boxEdit.setFocus();
    };
    
    this.slotCreate = function(){
        
    };
    
    this.slotRemove = function(trigger, element){
        // Remove product from GUI list
        this.list.delElement(element.getID());
        
        // Inform the repository for sync with server
        repository.removeElement(repository.repoProducts, element.getID());
    };
    
    this.slotEdit = function(trigger, element){
        element.lock();
        this.slotDblClick(trigger, element);
    };
    
    this.slotVignetteUpdate = function(element){
        // Refresh element at the repository
        repository.editElement(repository.repoProducts, element);
    };
    
    // ********************************************************* //
    // ** PRIVATE AUXILIARY METHODS                           ** //
    // ********************************************************* //
    
    this.getElementID = function(){
        return 'window_products';
    };
    
    this.generateList = function(){
        this.list = new gui_list('Productes');
        
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
    
    // Generate products array
    this.generateList();
    
    // Connect slots to the repository triggers
    var self = this;
    repository.addAdditionSlot(repository.repoProducts, function(json){self.slotRepoAdd(json);});
    repository.addDeletionSlot(repository.repoProducts, function(json){self.slotRepoDel(json);});
    repository.addUpdateSlot(repository.repoProducts, function(json){self.slotRepoUpd(json);});
    
    // Connect slots to the list triggers
    this.list.setDblClickSlot(function(trig, elem){self.slotDblClick(trig, elem);});
    this.list.setCreateSlot(function(){self.slotCreate();});
    this.list.setRemoveSlot(function(trigger, element){self.slotRemove(trigger, element);});
    this.list.setEditSlot(function(trigger, element){self.slotEdit(trigger, element);});
    
    // Start syncronization of products
    repository.activateRepository(repository.repoProducts);
}