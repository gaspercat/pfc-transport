function wdwDrivers(){
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
        var driver = new gui_wrapper_vignette(newDriverFromJson(json));
        
        // Add driver to list
        this.list.addElement(json['id'], json['name'], driver);
    };
    
    this.slotRepoDel = function(json){ 
        // If selected driver, remove driver view
        var elem = this.list.getSelectedElement();
        if(elem != null && elem.getID() == json['id']){
            this.removeDriverView();
        }
        
        // Remove driver from GUI list
        this.list.delElement(json['id']);
    };
    
    this.slotRepoUpd = function(json){
        var driver = new gui_wrapper_vignette(newDriverFromJson(json));
        
        // Update driver at GUI list
        this.list.updElement(json['id'], json['name'], driver);
        
        // If selected driver, update driver view
        var elem = this.list.getSelectedElement();
        if(elem != null && elem.getID() == json['id']){
            this.slotListClick(null, driver);
        }
    }
    
    this.slotListClick = function(trigger, element){
        var dates = element.getElement().getScheduledDates();
        var domElem;
        
        // Remove calendar if present
        domElem = document.getElementById(this.calendar.getDomID());
        if(domElem) domElem.removeNode();
        
        // Mark dates with schedules
        this.calendar.clear();
        this.calendar.setViewDate(this.calendar.getSelectedDate());
        for(var i=0;i<dates.length;i++){
            this.calendar.markDate(dates[i]);
        }
        
        // Draw calendar
        this.domElement.appendChild(this.calendar.draw());
        
        // Show schedule for selected date
        this.slotCalendarSelect(this.calendar.getSelectedDate());
    };
    
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
        
        // Remove driver view
        this.removeDriverView();
    };
    
    this.slotListEdit = function(trigger, element){
        element.lock();
        this.slotDblClick(trigger, element);
    };
    
    this.slotVignetteUpdate = function(element){
        // Refresh element at the repository
        repository.editElement(repository.repoDrivers, element);
    };
    
    this.slotCalendarSelect = function(date){
        var sched = this.list.getSelectedElement().getElement().getScheduleOnDate(date);
        var domElem;
        
        // Remove schedule if present
        domElem = document.getElementById(this.schedule.getDomID());
        if(domElem) domElem.removeNode();
       
        // Mark schedules for selected date
        this.schedule.clear();
        this.schedule.setTitle(date.toLongString());
        for(var i=0;i<sched.length;i++){
            var sch = sched[i];
            this.schedule.addInterval(sch['sh'], sch['sm'], sch['eh'], sch['em']);
        }
        
        // Draw schedule
        this.domElement.appendChild(this.schedule.draw());
    };
    
    this.slotScheduleSelect = function(){
        var schs = this.schedule.getIntervals();
        var elem = this.list.getSelectedElement().getElement();
        var date = this.calendar.getSelectedDate();
        
        // Replace schedule for selected date
        elem.delScheduleOnDate(date);
        for(var i=0;i<schs.length;i++){
            var sch = schs[i];
            elem.addSchedule(date.day, date.month, date.year, sch.stHour(), sch.stMinute(), sch.edHour(), sch.edMinute());
        }
        
        // Refresh element at the repository
        repository.editElement(repository.repoDrivers, elem);
    };
    
    // ********************************************************* //
    // ** PRIVATE AUXILIARY METHODS                           ** //
    // ********************************************************* //
    
    this.getElementID = function(){
        return 'window_drivers';
    };
    
    this.generateList = function(){
        this.list = new gui_list('Conductors');
        
        // Define list size & position
        this.list.setPosition(10, 10);
        this.list.setSize(200, 430);
        this.list.setEditable(true);
        
        // Set list's double click trigger
        var self = this;
        this.list.setDblClickSlot(function(trig, elem){self.slotListDblClick(trig, elem);});
    };
    
    this.generateCalendar = function(){
        this.calendar = new gui_calendar();
        
        // Define calendar size & position
        this.calendar.setPosition(700, 240);
        this.calendar.setSize(200, 200);
    };
    
    this.generateSchedule = function(){
        this.schedule = new gui_schedule();
        
        // Define calendar size & position
        this.schedule.setPosition(220, 240);
        this.schedule.setSize(470, 200);
        this.schedule.setTitle(this.calendar.getSelectedDate().toLongString());
    };
    
    this.removeDriverView = function(){
        var domElem;
        
        // Remove calendar view
        domElem = document.getElementById(this.calendar.getDomID());
        if(domElem) domElem.removeNode();
        
        // Remove schedule view
        domElem = document.getElementById(this.schedule.getDomID());
        if(domElem) domElem.removeNode();
    };
    
    // ********************************************************* //
    // ** CLASS CONSTRUCTOR                                   ** //
    // ********************************************************* //
    
    // Generate GUI elements
    this.generateList();
    this.generateCalendar();
    this.generateSchedule();
    
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
    
    // Connect slots to the calendar triggers
    this.calendar.setSelectSlot(function(date){self.slotCalendarSelect(date);});
    
    // Connect slots to the schedule triggers
    this.schedule.setSelectSlot(function(){ self.slotScheduleSelect(); });
    
    // Start syncronization of drivers
    repository.activateRepository(repository.repoDrivers);
}