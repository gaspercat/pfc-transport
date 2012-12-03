function Application(){
    this.menu = null;
    
    // Window objects
    this.wndUsers       = null;
    this.wndProducts    = null;
    this.wndDrivers     = null;
    this.wndTransports  = null;
    this.wndDepots      = null;
    this.wndClients     = null;
    
    // Selected window
    this.selected       = null;
    
    this.loadWindowProducts = function(){
        
    }
    
    // ********************************************************* //
    // ** PRIVATE :: EVENT HANDLERS                           ** //
    // ********************************************************* //
    
    this.slotShowUsers = function(){
        
    };
    
    this.slotShowProducts = function(){
        // Exit if it's the current window
        if(this.selected != null && this.selected == this.wndProducts) return;
        
        // Create window if doesn't exist
        if(this.wdwProducts == null) this.wdwProducts = new wdwProducts();
        
        // Draw window
        if(this.selected != null) this.selected.clean();
        document.body.appendChild(this.wdwProducts.draw());
        this.selected = this.wdwProducts;
        
        // Mark associated menu tab
        this.menu.selectTabData();
    };
    
    this.slotShowDrivers = function(){
        // Exit if it's the current window
        if(this.selected != null && this.selected == this.wndDrivers) return;
        
        // Create window if doesn't exist
        if(this.wdwDrivers == null) this.wdwDrivers = new wdwDrivers();
        
        // Draw window
        if(this.selected != null) this.selected.clean();
        document.body.appendChild(this.wdwDrivers.draw());
        this.selected = this.wdwDrivers;
        
        // Mark associated menu tab
        this.menu.selectTabData();
    };
    
    this.slotShowTransports = function(){
        
    };
    
    this.slotShowDepots = function(){
        
    };
    
    this.slotShowClients = function(){
        
    };
    
    // ********************************************************* //
    // ** PRIVATE :: AUXILIARY METHODS                        ** //
    // ********************************************************* //
    
    this.loadMenu = function(){
        this.menu = new wdwMenu(); 
        document.body.appendChild(this.menu.draw());
        
        // Connect tab triggers to slots
        var self = this;
        this.menu.setSlotUsers(function(){ self.slotShowUsers(); });
        this.menu.setSlotProducts(function(){ self.slotShowProducts(); })
        this.menu.setSlotDrivers(function(){ self.slotShowDrivers(); });
        this.menu.setSlotTransports(function(){ self.slotShowTransports(); });
        this.menu.setSlotDepots(function(){ self.slotShowDepots(); });
        this.menu.setSlotClients(function(){ self.slotShowClients(); });
    }
    
    // ********************************************************* //
    // ** CLASS CONSTRUCTOR                                   ** //
    // ********************************************************* //
    
    // Load menu
    this.loadMenu();
}

// ********************************************************* //
// ** APPLICATION LOADER                                  ** //
// ********************************************************* //

window.onload = function(){
    // Load global event handlers
    globalEvents = new GlobalEvents();
    
    // Initialize repository
    repository = new Repository();

    // Create application
    application = new Application();
};