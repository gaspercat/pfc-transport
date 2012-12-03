function wdwMenu(){
    // GUI elements
    this.tabs = null;
    this.optsData = null;
    this.optsTasks = null;
    this.optsPlanning = null;
    
    // Slots
    this.slotusers = null;
    this.slotproducts = null;
    this.slotdrivers = null;
    this.slottransports = null;
    this.slotdepots = null;
    this.slotclients = null;
    
    // DOM elements
    this.domElement = null;
    
    // ********************************************************* //
    // ** PUBLIC :: SETTERS                                   ** //
    // ********************************************************* //
    
    this.setSlotUsers = function(funct){
        this.slotusers = funct;
    };
    
    this.setSlotProducts = function(funct){
        this.slotproducts = funct;
    };
    
    this.setSlotDrivers = function(funct){
        this.slotdrivers = funct;
    };
    
    this.setSlotTransports = function(funct){
        this.slottransports = funct;
    };
    
    this.setSlotDepots = function(funct){
        this.slotdepots = funct;
    };
    
    this.setSlotClients = function(funct){
        this.slotclients = funct;
    };
    
    // ********************************************************* //
    // ** PUBLIC :: ACTUATORS                                 ** //
    // ********************************************************* //
    
    this.selectTabUsers = function(){
        this.tabs.selTab(1);
    };
    
    this.selectTabData = function(){
        this.tabs.selTab(2);
    };
    
    this.selectTabTasks = function(){
        this.tabs.selTab(3);
    };
    
    this.selectTabPlannings = function(){
        this.tabs.selTab(4);
    };
    
    // ********************************************************* //
    // ** PUBLIC :: DRAWING METHODS                           ** //
    // ********************************************************* //
    
    this.draw = function(){
        // Draw menu bar
        var ret = document.createElement('div');
            ret.setAttribute('class', 'window_menu');
            ret.style.display = 'block';
            ret.style.position = 'absolute';
            ret.style.width = '100%';
            ret.style.height = '72px';
            ret.style.backgroundColor = '#FFF';
            ret.style.border = '1px solid #CCC';
            
            // Draw menu decoration
            var deco = document.createElement('div');
                deco.style.display = 'block';
                deco.style.position = 'absolute';
                deco.style.width = '160px';
                deco.style.height = '100px';
                deco.style.top = '0px';
                deco.style.left = '851px';
                deco.style.backgroundImage = 'url(\'/img/truckbar.png\')';
            ret.appendChild(deco);
            
            // Add logout button
            var btLogout = document.createElement('div');
                btLogout.setAttribute('class', 'bt_logout');
                btLogout.style.display = 'block';
                btLogout.style.position = 'absolute';
                btLogout.style.left = '875px';
                btLogout.style.top = '48px';
            ret.appendChild(btLogout);
            
            // Add tabs
            ret.appendChild(this.tabs.draw());
                
            
        this.domElement = ret;
        return ret;
    };
    
    // ********************************************************* //
    // ** PRIVATE :: TRIGGER HANDLERS                         ** //
    // ********************************************************* //
    
    this.slotTabUsers = function(){
        if(this.slotusers) this.slotusers();
    };
    
    this.slotTabData = function(){
        // Draw menu
        var domElem = this.optsData.draw();
        this.domElement.appendChild(domElem);
    };
    
    this.slotTabTasks = function(){
        
    };
    
    this.slotTabPlannings = function(){
        
    };
    
    this.slotButtonProducts = function(){
        if(this.slotproducts) this.slotproducts();
    };
    
    this.slotButtonDrivers = function(){
        if(this.slotdrivers) this.slotdrivers();
    };
    
    this.slotButtonTransports = function(){
        if(this.slottransports) this.slottransports();
    };
    
    this.slotButtonDepots = function(){
        if(this.slotdepots) this.slotdepots();
    };
    
    this.slotButtonClients = function(){
        if(this.slotclients) this.slotclients();
    };
    
    // ********************************************************* //
    // ** PRIVATE :: AUXILIARY METHODS                        ** //
    // ********************************************************* //
    
    this.generateTabs = function(){
        var tabs = new gui_tabs();
        
        // Position tabs
        tabs.setPosition(20, 43);
        tabs.setSize(620, 30);
        
        // Add tabs
        var self = this;
        tabs.addTabLeft(1, 'Usuaris', function(){ self.slotTabUsers(); })
        tabs.addTabRight(2, 'Dades', function(){ self.slotTabData(); });
        tabs.addTabRight(3, 'Tasques', function(){ self.slotTabTasks(); });
        tabs.addTabRight(4, 'Recorreguts', function(){ self.slotTabPlannings(); });
        
        this.tabs = tabs;
    };
    
    this.generateOptionsData = function(){
        var menu = new gui_menu();
        
        // Position menu
        menu.setPosition(272, 73);
        
        // Add buttons
        var self = this;
        menu.addButton(1, 'Productes', function(){ self.slotButtonProducts(); });
        menu.addButton(2, 'Conductors', function(){ self.slotButtonDrivers(); });
        menu.addButton(3, 'Transports', function(){ self.slotButtonTransports(); });
        menu.addButton(4, 'Dipòsits', function(){ self.slotButtonDepots(); });
        menu.addButton(5, 'Clients', function(){ self.slotButtonClients(); });
        
        this.optsData = menu;
    };
    
    this.generateOptionsTasks = function(){

    };
    
    this.generateOptionsPlannings = function(){
        
    };
    
    // ********************************************************* //
    // ** CLASS CONSTRUCTOR                                   ** //
    // ********************************************************* //
    
    // Generate GUI elements
    this.generateTabs();
    this.generateOptionsData();
    this.generateOptionsTasks();
    this.generateOptionsPlannings();
}