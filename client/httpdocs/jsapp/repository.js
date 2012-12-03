function Repository(){
    // Basic repositories
    this.repoClients     = new Array();
    this.repoDepots      = new Array();
    this.repoProducts    = new Array();
    this.repoDrivers     = new Array();
    this.repoTransports  = new Array();
    this.repoUsers       = new Array();
    
    // ********************************************************* //
    // ** PUBLIC :: ACTUATORS                                 ** //
    // ********************************************************* //
    
    this.activateRepository = function(repo){
        repo.active = true;
        this.syncRepository(repo);
    };
    
    this.deactivateRepository = function(repo){
        repo['active'] = false;
    };
    
    this.addAdditionSlot = function(repo, funct){
        repo['add'][repo['add'].length] = funct;
    };
    
    this.addDeletionSlot = function(repo, funct){
        repo['del'][repo['del'].length] = funct;
    };
    
    this.addUpdateSlot = function(repo, funct){
        repo['upd'][repo['upd'].length] = funct;
    };
    
    // ********************************************************* //
    // ** PUBLIC :: CONTENT MANAGEMENT                        ** //
    // ********************************************************* //
    
    this.addElement = function(repo, element){
        var decoded = element.jsonStructure();
        
        // Add element to the repository list
        repo['elements'][repo['elements'].length] = decoded;
        
        // Append "act":"add" to request
        decoded['act'] = 'add';
        
        // Add action to the sync queue
        repo['updates'][repo['updates'].length] = decoded;
        
        // Send remove signal to the associated slots
        this.signalAddition(repo, decoded);
    };
    
    this.removeElement = function(repo, id){
        var decoded, commit = true;
        var i;
        
        // Remove element from the repository list
        for(i=0;i<repo['elements'].length;i++){
            if(repo['elements']['id'] == id){
                repo['elements'].splice(i, 1);
                break;
            }
        }
        
        // Create "act":"del" sync request
        decoded = new Object();
        decoded['act'] = 'del';
        decoded['id'] = id;
        
        // Clear previous conflicting sync requests
        commit = true;
        for(i=repo['updates'].length;i>=0;i--){
            if(repo['updates'][i]['id'] == id){
                if(repo['updates'][i]['act'] == 'add') commit = false;
                repo['updates'].splice(i, 1);
            }
        }
        
        // If needed, enqueue sync request
        if(commit == true){
            repo['updates'][repo['updates'].length] = decoded;
        }
        
        // Send remove signal to the associated slots
        this.signalDeletion(repo, decoded);
    };
    
    this.editElement = function(repo, element){
        var decoded = element.jsonStructure();
        var commit, i;
        
        // Replace element at the repository list
        for(i=0;i<repo['elements'].length;i++){
            if(repo['elements']['id'] == element.getID()){
                repo['elements'][i] = decoded;
                break;
            }
        }
        
        // Append "act":"upd" to request
        decoded['act'] = 'upd';
        
        // Replace previous element update if present
        commit = true;
        for(i=0;i<repo['updates'].length;i++){
            if(repo['updates'][i]['id'] == decoded['id'] && repo['updates'][i]['act'] == 'upd'){
                repo['updates'][i] = decoded;
                commit = false;
            }
        }
        
        // If needed, enqueue sync request
        if(commit == true){
            repo['updates'][repo['updates'].length] = decoded;
        }
        
        // Send update signal to the associated slots
        this.signalUpdate(repo, decoded);
    };
    
    // ********************************************************* //
    // ** PRIVATE :: AUXILIARY METHODS                        ** //
    // ********************************************************* //
    
    this.initRepository = function(name, repo){
        // Initialize attributes
        repo['active'] = false;
        repo['version'] = -1;
        repo['name'] = name;
        
        // Initialize content
        repo['elements'] = new Array();
        repo['updates'] = new Array();
        
        // Initialize external slots (for triggers)
        repo['add'] = new Array();
        repo['del'] = new Array();
        repo['upd'] = new Array();
    };
    
    this.timerSynchronize = function(){
        // Synchronize repositories
        this.syncRepository(this.repoUsers);
        this.syncRepository(this.repoProducts);
        this.syncRepository(this.repoDrivers);
        this.syncRepository(this.repoTransports);;
        this.syncRepository(this.repoDepots);
        this.syncRepository(this.repoClients);
        
        // Prepare next sync
        setTimeout('repository.timerSynchronize();', 15000);
    };
    
    this.syncRepository = function(repo){
        var self = this;
        var req, resp, i;

        // ** Upload changes to the server
        // ************************************************

        if(repo['updates'].length > 0){
            var nupd = repo['updates'].length;
            
            // Prepare request parameter
            req = '{"act":"synu","repo":"' + repo['name'] + '","elems":[';
            for(i=0;i<nupd;i++){
                req = req + encodeJson(repo['updates'][i]);
                if(i != repo['updates'].length - 1) req = req + ',';
            }
            req = req + ']}';
            
            // Prepare response function
            resp = function(response){
                var parse = decodeJson(response);
                if((!parse['status']) || parse['status'] != 'ok') return;
                
                // Clear updated elements
                repo['updates'].splice(0, nupd);
            }
            
            // Send request
            requestAjax('/en/query', req, resp);
        }
        
        // ** Synchronize repository to server's version
        // ************************************************
        
        // If repository is inactive, don't syncronize
        if(repo['active'] == false) return;
        
        // Prepare request parameter
        req = '{"act":"synd","repo":"' + repo['name'] + '","ver":' + repo['version'] + '}';
        req = "req=" + encodeURIComponent(req);

        // Prepare response function
        resp = function(response){
            var parse = decodeJson(response);
            if((!parse['status']) || parse['status'] != 'ok') return;
            
            // Update synchronization version
            if(repo['version'] == parse['ver']) return;
            repo['version'] = parse['ver'];
            
            // Get returned elements
            var list = parse['elems'];
            if(!list) return;
            
            // Process new elements
            for(var i=0;i<list.length;i++){
                var found = false;
                
                // Update element if exists
                for(var j=0;j<repo['elements'].length;j++){
                    if(repo['elements'][j]['id'] == list[i]['id']){
                        if(list[i]['act'] == 'del'){
                            repo['elements'].splice(j, 1);
                            self.signalDeletion(repo, list[i]);
                        }else{
                            repo['elements'][j] = list[i];
                            delete repo['elements'][j]['act'];
                            self.signalUpdate(repo, list[i]);
                            found = true;
                        }
                        continue;
                    }
                }
                
                // Create element if it doesn't exist
                if(found == false && list[i]['act'] != 'del'){
                    var idx = repo['elements'].length;
                    repo['elements'][idx] = list[i];
                    delete repo['elements'][idx]['act'];
                    self.signalAddition(repo, list[i]);
                }
            }
        };

        // Send request
        requestAjax('/en/query', req, resp);
    };
    
    // ********************************************************* //
    // ** PRIVATE :: SIGNALING METHODS                        ** //
    // ********************************************************* //
    
    // Emit an element addition message
    this.signalAddition = function(repo, json){
        for(var i=0;i<repo['add'].length;i++){
            repo['add'][i](json);
        }
    };
    
    // Emit an element deletion message
    this.signalDeletion = function(repo, json){
        for(var i=0;i<repo['del'].length;i++){
            repo['del'][i](json);
        }
    };
    
    // Emit an element update message
    this.signalUpdate = function(repo, json){
        for(var i=0;i<repo['upd'].length;i++){
            repo['upd'][i](json);
        }
    };
    
    // ********************************************************* //
    // ** CLASS CONSTRUCTOR                                   ** //
    // ********************************************************* //
    
    // Initialize repositories
    this.initRepository("users",      this.repoUsers);
    this.initRepository("products",   this.repoProducts);
    this.initRepository("drivers",    this.repoDrivers);
    this.initRepository("transports", this.repoTransports);
    this.initRepository("depots",     this.repoDepots);
    this.initRepository("clients",    this.repoClients);
    
    // Start synchronization
    this.timerSynchronize();
}