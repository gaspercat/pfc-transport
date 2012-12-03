function gui_element_driver(id, name, username, password, cellnum){
    this.id = id;                 // ID of the driver
    this.name = name;             // Full name of the driver
    this.username = username;     // Mobile APP username
    this.password = password;     // Mobile APP password
    this.cellnum = cellnum;       // Cell number
    this.schedule = new Array();  // Driver schedule
    
    // Edition DOM elements
    this.fName = new gui_field_text();
    this.fName.setValue(name);
    this.fUsername = new gui_field_text();
    this.fUsername.setValue(username);
    this.fPassword = new gui_field_text();
    this.fPassword.setValue(password);
    this.fCellnum = new gui_field_numeric();
    this.fCellnum.setValue(cellnum);
    
    // ********************************************************* //
    // ** PUBLIC :: GETTERS                                   ** //
    // ********************************************************* //
    
    this.getID = function(){
        return this.id;
    };
    
    this.getName = function(){
        return this.name;
    };
    
    this.getUsername = function(){
        return this.username;
    };
    
    this.getPassword = function(){
        return this.password;
    };
    
    this.getCellNumber = function(){
        return this.cellnum;
    };
    
    this.getScheduledDates = function(){
        var ret = new Array();
        
        for(var i=0;i<this.schedule.length;i++){
            var sch = this.schedule[i];
            ret[ret.length] = new TypeDate(sch['day'], sch['month'], sch['year']);
        }
        
        return ret;
    };
    
    this.getScheduleOnDate = function(date){
        var ret = new Array();
        
        for(var i=0;i<this.schedule.length;i++){
            var sch = this.schedule[i];
            if(sch['year'] == date.year && sch['month'] == date.month && sch['day'] == date.day){
                ret[ret.length] = sch;
            }
        }
        
        return ret;
    };
    
    // ********************************************************* //
    // ** PUBLIC :: SETTERS                                   ** //
    // ********************************************************* //
    
    this.setName = function(name){
        this.name = name;
        this.fName.setValue(name);
    };
    
    this.setUsername = function(username){
        this.username = username;
        this.fUsername.setValue(username);
    };
    
    this.setPassword = function(password){
        this.password = password;
        this.fPassword.setValue(password);
    };
    
    this.setCellNumber = function(cellnum){
        this.cellnum = cellnum;
        this.fCellnum.setValue(cellnum);
    };
    
    this.setFocus = function(){
        this.fName.setFocus();
    };
    
    // ********************************************************* //
    // ** PUBLIC :: CONTENT MANAGEMENT                        ** //
    // ********************************************************* //
    
    this.addSchedule = function(day, month, year, sh, sm, eh, em){
        var i;
        
        // Create new scheduling object
        var sch = this.createSchedule(day, month, year, sh, sm, eh, em);
        
        // Try to overlap with schedules for that date
        for(i=0;i<this.schedule.length;i++){
            var tsch = this.schedule[i];
            if(this.schedulesOverlap(sch, tsch) == true){
                // Merge schedules
                sch = this.mergeSchedules(sch, tsch);
                this.schedule.splice(i, 1);
            }
        }
        
        // Insert to schedules list
        this.schedule[this.schedule.length] = sch;
    };
    
    this.delScheduleOnDate = function(date){
        for(var i=0;i<this.schedule.length;i++){
            var tsch = this.schedule[i];
            if(tsch['year'] == date.year && tsch['month'] == date.month && tsch['day'] == date.day){
                this.schedule.splice(i, 1);
            }
        }
    };
    
    // ********************************************************* //
    // ** PUBLIC :: ACTUATORS                                 ** //
    // ********************************************************* //
    
    /**
     * Check if the update form parameters are correctly formatted
     * @return true if valid, an error message if invalid
     */
    this.checkUpdateForm = function(){
        var n, i;
        
        var name = this.fName.getValue();
        var username = this.fUsername.getValue();
        var password = this.fPassword.getValue();
        var cellnum = this.fCellnum.getValue();
        
        // Check name isn't empty
        n = 0;
        for(i=0;i<name.length;i++){
            if(name.charAt(i) != ' ') n++;
        }
        if(n == 0) return "El nom no pot ser un camp buit";
        
        // Check username isn't empty
        n = 0;
        for(i=0;i<username.length;i++){
            if(username.charAt(i) != ' ') n++;
        }
        if(n == 0) return "El nom d'usuari no pot ser un camp buit";
        
        // Check password isn't empty
        n = 0;
        for(i=0;i<password.length;i++){
            if(password.charAt(i) != ' ') n++;
        }
        if(n == 0) return "La contrasenya no pot ser un camp buit";

        // Return true upon success
        return true;
    };
    
    this.updateFromForm = function(){
        this.name = this.fName.getValue();
        this.username = this.fUsername.getValue();
        this.password = this.fPassword.getValue();
        this.cellnum = this.fCellnum.getValue();
    };
    
    this.jsonStructure = function(){
        var ret = new Object();
        
        ret['id'] = this.id;
        ret['name'] = this.name;
        ret['username'] = this.username;
        ret['password'] = this.password;
        ret['cellnum'] = this.cellnum;
        ret['schedule'] = this.schedule;
        
        return ret;
    };
    
    // ********************************************************* //
    // ** PUBLIC :: DRAWING METHODS                           ** //
    // ********************************************************* //
    
    this.draw = function(){
        var ret, tr, td;
        
        ret = document.createElement('table');
            ret.style.margin = 'auto';
            ret.style.borderCollapse = 'collapse';
            
            tr = document.createElement('tr');
                td = document.createElement('td');
                    td.style.height = '24px';
                    td.style.textAlign = 'right';
                    td.appendChild(document.createTextNode('Nom:')); 
                tr.appendChild(td);
            
                td = document.createElement('td');
                    td.style.textAlign = 'left';
                    td.style.paddingLeft = '10px';
                    td.appendChild(this.fName.draw());
                tr.appendChild(td);
            
            ret.appendChild(tr);
            
            tr = document.createElement('tr');
                td = document.createElement('td');
                    td.style.height = '24px';
                    td.style.paddingTop = '4px';
                    td.style.textAlign = 'right';
                    td.appendChild(document.createTextNode('Usuari:')); 
                tr.appendChild(td);
            
                td = document.createElement('td');
                    td.style.textAlign = 'left';
                    td.style.paddingLeft = '10px';
                    td.style.paddingTop = '4px';
                    td.appendChild(this.fUsername.draw());
                tr.appendChild(td);
            
            ret.appendChild(tr);
            
            tr = document.createElement('tr');
                td = document.createElement('td');
                    td.style.height = '24px';
                    td.style.paddingTop = '4px';
                    td.style.textAlign = 'right';  
                    td.appendChild(document.createTextNode('Contrasenya:')); 
                tr.appendChild(td);
            
                td = document.createElement('td');
                    td.style.textAlign = 'left';
                    td.style.paddingLeft = '10px';
                    td.style.paddingTop = '4px';
                    td.appendChild(this.fPassword.draw());
                tr.appendChild(td);
            
           ret.appendChild(tr);
           
            tr = document.createElement('tr');
                td = document.createElement('td');
                    td.style.height = '24px';
                    td.style.paddingTop = '4px';
                    td.style.textAlign = 'right';  
                    td.appendChild(document.createTextNode('Num. Mòbil:')); 
                tr.appendChild(td);
            
                td = document.createElement('td');
                    td.style.textAlign = 'left';
                    td.style.paddingLeft = '10px';
                    td.style.paddingTop = '4px';
                    td.appendChild(this.fCellnum.draw());
                tr.appendChild(td);
            
           ret.appendChild(tr);
            
           tr = document.createElement('tr');
               td = document.createElement('td');
                   td.setAttribute('colspan', '2');
               tr.appendChild(td);
           ret.appendChild(tr);
            
        return ret;
    };
    
    this.drawSummary = function(){
        var tr, td;
        
        var ret = document.createElement('table');
            ret.style.backgroundColor = '#FFF';
            ret.style.borderCollapse = 'collapse';
            ret.style.width = '100%';
            ret.style.height = '100%';
            
            tr = document.createElement('tr');
                td = document.createElement('td');
                    td.style.paddingLeft = '15px';
                    td.style.height = '18px';
                    td.style.fontSize = '14px';
                    td.appendChild(document.createTextNode('Usuari'));
                tr.appendChild(td);
                td = document.createElement('td');
                    td.style.paddingLeft = '15px';
                    td.style.fontSize = '14px';
                    td.appendChild(document.createTextNode(this.username));
                tr.appendChild(td);
            ret.appendChild(tr);
            
            tr = document.createElement('tr');
                td = document.createElement('td');
                    td.style.paddingLeft = '15px';
                    td.style.height = '18px';
                    td.style.fontSize = '14px';
                    td.appendChild(document.createTextNode('Contrasenya'));
                tr.appendChild(td);
                td = document.createElement('td');
                    td.style.paddingLeft = '15px';
                    td.style.fontSize = '14px';
                    td.appendChild(document.createTextNode(this.password));
                tr.appendChild(td);
            ret.appendChild(tr);
            
            tr = document.createElement('tr');
                td = document.createElement('td');
                    td.style.paddingLeft = '15px';
                    td.style.height = '18px';
                    td.style.fontSize = '14px';
                    td.appendChild(document.createTextNode('Num. Mòbil'));
                tr.appendChild(td);
                td = document.createElement('td');
                    td.style.paddingLeft = '15px';
                    td.style.fontSize = '14px';
                    td.appendChild(document.createTextNode(this.cellnum));
                tr.appendChild(td);
            ret.appendChild(tr);
                
        return ret;
    };
    
    // ********************************************************* //
    // ** PRIVATE :: AUXILIARY METHODS                        ** //
    // ********************************************************* //
    
    /**
     * Create a schedule element from the passed date, start time and
     * end time.
     * @return A schedule
     */
    this.createSchedule = function(day, month, year, sh, sm, eh, em){
        var sch = new Object();
        
        sch['day'] = day;
        sch['month'] = month;
        sch['year'] = year;
        sch['sh'] = sh;
        sch['sm'] = sm;
        sch['eh'] = eh;
        sch['em'] = em;
        
        return sch;
    };
    
    this.compareDates = function(sch1, sch2){
        if(sch1['year'] != sch2['year']) return false;
        if(sch1['month'] != sch2['month']) return false;
        if(sch1['day'] != sch2['day']) return false;
        return true;
    };
    
    /**
     * Compares the two times
     * @return -1 if t1 < t2, 0 if equal, 1 if t1 > t2
     */
    this.compareTimes = function(h1, m1, h2, m2){
        if(h1 < h2) return -1;
        if(h1 > h2) return 1;
        if(m1 < m2) return -1;
        if(m1 > m2) return 1;
        return 0;
    };
    
    /**
     * Check if the two schedules have any overlap over time
     * @return true if overlap, false otherwise
     */
    this.schedulesOverlap = function(sch1, sch2){
        if(this.compareDates(sch1, sch2) == false) return false;
        var c1, c2
        
        // Compare start times
        c1 = this.compareTimes(sch1['sh'], sch1['sm'], sch2['sh'], sch2['sm']);
        if(c1 <= 0){
            // Compare end of first with start of second
            c2 = this.compareTimes(sch1['eh'], sch1['em'], sch2['sh'], sch2['sm']);
            return (c2 >= 0);
        }else{
            // Compare end of second with start of first
            c2 = this.compareTimes(sch2['eh'], sch2['em'], sch1['sh'], sch1['sm']);
            return (c2 >= 0);
        }
    };
    
    /**
     * Merge both schedules to a new one which contain both schedules in a single
     * interval with the minimum start time and the maximum end time.
     * @return A new schedule which contains both passed schedules intervals
     */
    this.mergeSchedules = function(sch1, sch2){
        var ret = new Object();
        var c;
        
        // Set date
        ret['year'] = sch1['year'];
        ret['month'] = sch1['month'];
        ret['day'] = sch1['day'];
        
        // Set start time
        c = this.compareTimes(sch1['sh'], sch1['sm'], sch2['sh'], sch2['sm']);
        if(c <= 0){
            ret['sh'] = sch1['sh'];
            ret['sm'] = sch1['sm'];
        }else{
            ret['sh'] = sch2['sh'];
            ret['sm'] = sch2['sm'];
        }
        
        // Set end time
        c = this.compareTimes(sch1['eh'], sch1['em'], sch2['eh'], sch2['em']);
        if(c >= 0){
            ret['eh'] = sch1['eh'];
            ret['em'] = sch1['em'];
        }else{
            ret['eh'] = sch2['eh'];
            ret['em'] = sch2['em'];
        }
        
        return ret;
    };
}

function newDriverFromJson(json){
    var driver = new gui_element_driver(json['id'], json['name'], json['username'], json['password'], json['cellnum']);
    
    var scheds = json['schedule'];
    for(var i=0;i<scheds.length;i++){
        var sched = scheds[i];
        driver.addSchedule(sched['day'], sched['month'], sched['year'], sched['sh'], sched['sm'], sched['eh'], sched['em']);
    }
        
    return driver;
}