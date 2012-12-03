function GlobalEvents(){
    this.slotclk = new Array();
    this.slotdbclk = new Array();
    
    // ********************************************************* //
    // ** PUBLIC :: SIGNALS MANAGEMENT                        ** //
    // ********************************************************* //
    
    this.addClickSlot = function(funct){
        this.slotclk[this.slotclk.length] = funct;
    };
    
    this.addDblClickSlot = function(funct){
        this.slotdbclk[this.triggersDblClick.length] = funct;
    };
    
    // ********************************************************* //
    // ** PRIVATE :: SIGNALING METHODS                        ** //
    // ********************************************************* //
    
    this.signalClick = function(){
        for(var i=0;i<this.slotclk.length;i++){
            this.slotclk[i]();
        }
    };
    
    this.signalDblClick = function(){
        for(var i=0;i<this.slotdbclk.length;i++){
            this.slotdbclk[i]();
        }
    };
    
    // ********************************************************* //
    // ** CLASS CONSTRUCTOR                                   ** //
    // ********************************************************* //
    
    // Initialize events
    var self = this;
    document.body.onclick = function(){ self.signalClick(); };
    document.body.ondblclick = function(){ self.signalDblClick(); };
}