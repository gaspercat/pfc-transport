gui_calendar_count = 0;

function gui_calendar(){
    this.id = gui_calendar_count;
    gui_calendar_count++;
    
    // Positioning attributes
    this.left = 0;
    this.top = 0;
    this.width = 200;
    this.height = 200;
    
    // Content atributes
    this.cdate = null;                // Current date
    this.vdate = null;                // View date (defines the calendar page)
    this.sdate = null;                // Selected date
    this.marks = new Object();        // Calendary marks
    
    // Slots for calendar signals
    this.slotselect = null;
    
    // ********************************************************* //
    // ** PUBLIC :: GETTERS                                   ** //
    // ********************************************************* //
    
    this.getID = function(){
        return this.id;
    };
    
    this.getDomID = function(){
        return this.getElementID();
    }
    
    this.getSelectedDate = function(){
        return this.sdate.copy();
    };
    
    this.getCurrentDate = function(){
        return this.cdate.copy();
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
    
    this.setSelectedDate = function(date){
        this.vdate = date.copy();
        
        // Redraw calendar
        var domElem = document.getElementById(this.getElementID());
        if(domElem) domElem.replaceNode(this.draw());
    };
    
    this.setViewDate = function(date){
        this.vdate = date.copy();
        this.vdate.day = 1;
        
        // Redraw calendar
        var domElem = document.getElementById(this.getElementID());
        if(domElem) domElem.replaceNode(this.draw());
    };
    
    // ********************************************************* //
    // ** PUBLIC :: SIGNALS MANAGEMENT                        ** //
    // ********************************************************* //
    
    this.setSelectSlot = function(funct){
        this.slotselect = funct;
    };
    
    // ********************************************************* //
    // ** PUBLIC :: CONTENT MANAGEMENT                        ** //
    // ********************************************************* //
    
    this.clear = function(){
        // Remove marks
        this.marks = new Object();
        
        // Redraw calendar
        var domElem = document.getElementById(this.getElementID());
        if(domElem) domElem.replaceNode(this.draw())
    };
    
    this.markDate = function(date){
        var domElem;
        
        // Find date mark
        if(!this.marks[date.year]) this.marks[date.year] = new Object();
        if(!this.marks[date.year][date.month]) this.marks[date.year][date.month] = new Object();
        if(!this.marks[date.year][date.month][date.day]){
            // Mark if doesn't exist
            this.marks[date.year][date.month][date.day] = true;
            
            // Redraw calendar
            domElem = document.getElementById(this.getElementID());
            if(domElem) domElem.replaceNode(this.draw());
        }
    };
    
    this.umarkDate = function(date){
       // Check mark exists
       if(!this.marks[date.year]) return;
       if(!this.marks[date.year][date.month]) return;
       if(!this.marks[date.year][date.month][date.day]) return;
       
       // Remove mark
       delete this.marks[date.year][date.month][date.day];
    };
    
    // ********************************************************* //
    // ** PUBLIC :: ACTUATORS                                 ** //
    // ********************************************************* //
    
    this.selectDate = function(day, month, year){
        // Mark the selected date
        this.sdate = new TypeDate(day, month, year);
        
        // Move the calendar to the selected date's month
        this.vdate.month = month;
        this.vdate.year = year;
        
        // Redraw the calendar
        var domElem = document.getElementById(this.getElementID());
        if(domElem) domElem.replaceNode(this.draw());
    };
    
    // ********************************************************* //
    // ** PUBLIC :: DRAWING METHODS                           ** //
    // ********************************************************* //
    
    this.draw = function(){
        var ret, table, tr, td;
        
        var self = this;
        var y = this.vdate.year;
        var m = this.vdate.month;
        
        ret = document.createElement('div');
            ret.setAttribute('id', this.getElementID());
            ret.style.position = 'absolute';
            ret.style.left = this.left + 'px';
            ret.style.top = this.top + 'px';
            ret.style.width = (this.width - 10) + 'px';
            ret.style.height = (this.height - 10) + 'px';
            ret.style.backgroundColor = '#EEE';
            ret.style.padding = '5px';
            
            table = document.createElement('table');
                table.style.width = '100%';
                table.style.height = '100%';
                table.style.borderCollapse = 'collapse';
            
                tr = document.createElement('tr');
                    tr.style.height = '25px';
                    tr.style.backgroundColor = '#EEE';
                    
                    td = document.createElement('td');
                        td.setAttribute('class', 'gui_calendar_left');
                        td.appendChild(document.createTextNode('<'));
                        td.onclick = function(){
                            // Move one month backward
                            self.vdate = self.getPreviousMonth(self.vdate);

                            // Redraw calendar
                            var domElem = document.getElementById(self.getElementID());
                            if(domElem) domElem.replaceNode(self.draw());
                        }
                    tr.appendChild(td);

                    td = document.createElement('td');
                        td.style.textAlign = 'center';
                        td.appendChild(document.createTextNode(this.getMonthName(m) + ' ' + y));
                    tr.appendChild(td);

                    td = document.createElement('td');
                        td.setAttribute('class', 'gui_calendar_right');
                        td.appendChild(document.createTextNode('>'));
                        td.onclick = function(){
                            // Move one month forward
                            self.vdate = self.getNextMonth(self.vdate);

                            // Redraw calendar
                            var domElem = document.getElementById(self.getElementID());
                            if(domElem) domElem.replaceNode(self.draw());
                        };
                    tr.appendChild(td);

                table.appendChild(tr);

                tr = document.createElement('tr');
                    td = document.createElement('td');
                        td.setAttribute('colspan', 3);
                        td.style.backgroundColor = '#FFF';
                        td.appendChild(this.drawMonth());
                    tr.appendChild(td);
                table.appendChild(tr);
                     
            ret.appendChild(table);
            
        return ret;
    };
    
    // ********************************************************* //
    // ** PRIVATE :: DRAWING METHODS                          ** //
    // ********************************************************* //
    
    this.drawMonth = function(){
        var ret, tr, td;
        
        // Get selected year & month
        var y = this.vdate.year;
        var m = this.vdate.month;
        
        // Get table position of day 1
        var fpos = (new Date(y, m-1, 1)).getDay() - 1;
        if(fpos == -1) fpos = 6;
        
        // Get days in month
        var mdays_prev = this.getMonthDays(y, (m > 1) ? m - 1 : 12);
        var mdays_act = this.getMonthDays(y, m);
        
        
        ret = document.createElement('table');
            ret.style.width = '100%';
            ret.style.height = '100%';
            ret.style.borderCollapse = 'collapse';
        
            // Draw names for week days
            tr = document.createElement('tr');
                // Monday
                td = document.createElement('td');
                    td.style.textAlign = 'center';
                    td.style.fontSize = '13px';
                    td.style.paddingTop = '4px';
                    td.appendChild(document.createTextNode('DL'));
                tr.appendChild(td);
                // Tuesday
                td = document.createElement('td');
                    td.style.textAlign = 'center';
                    td.style.fontSize = '13px';
                    td.style.paddingTop = '4px';
                    td.appendChild(document.createTextNode('DM'));
                tr.appendChild(td);
                // Wednesday
                td = document.createElement('td');
                    td.style.textAlign = 'center';
                    td.style.fontSize = '13px';
                    td.style.paddingTop = '4px';
                    td.appendChild(document.createTextNode('DC'));
                tr.appendChild(td);
                // Thursday
                td = document.createElement('td');
                    td.style.textAlign = 'center';
                    td.style.fontSize = '13px';
                    td.style.paddingTop = '4px';
                    td.appendChild(document.createTextNode('DJ'));
                tr.appendChild(td);
                // Fryday
                td = document.createElement('td');
                    td.style.textAlign = 'center';
                    td.style.fontSize = '13px';
                    td.style.paddingTop = '4px';
                    td.appendChild(document.createTextNode('DV'));
                tr.appendChild(td);
                // Saturday
                td = document.createElement('td');
                    td.style.textAlign = 'center';
                    td.style.fontSize = '13px';
                    td.style.paddingTop = '4px';
                    td.appendChild(document.createTextNode('DS'));
                tr.appendChild(td);
                // Sunday
                td = document.createElement('td');
                    td.style.textAlign = 'center';
                    td.style.fontSize = '13px';
                    td.style.paddingTop = '4px';
                    td.appendChild(document.createTextNode('DM'));
                tr.appendChild(td);
            ret.appendChild(tr);
            
            for(var i=0;i<6;i++){
                tr = document.createElement('tr');
                for(var j=0;j<7;j++){
                    // Calculate date to show
                    var belongs = true;
                    var month = m;
                    var year = y;
                    var dpos = (i*7 + j) - fpos + 1;
                    if(dpos > mdays_act){
                        dpos = dpos - mdays_act;
                        month++;
                        if(month > 12){
                            month = 1;
                            year++;
                        }
                        belongs = false;
                    }else if(dpos < 1){
                        dpos = mdays_prev + dpos;
                        month--;
                        if(month < 1){
                            month = 12;
                            year--;
                        }
                        belongs = false;
                    }
                    
                    // Draw day
                    tr.appendChild(this.drawDay(dpos, month, year, belongs));
                }
                ret.appendChild(tr);
            }
        
        return ret;
    };
    
    this.drawDay = function(day, month, year, belongs){
        var self = this;
        
        var cdate = this.cdate;
        
        var ret = document.createElement('td');
            ret.style.width = '14.29%';
            ret.style.textAlign = 'center';
            ret.style.verticalAlign = 'middle';
            ret.style.fontSize = '13px';
            ret.style.color = (belongs) ? '#000' : '#BBB' ;
            ret.style.paddingTop = '4px';
            ret.appendChild(document.createTextNode(day));
            
            ret.onclick = function(){
                // Select date
                self.sdate = new TypeDate(day, month, year);
                
                // Redraw calendar
                var domElem = document.getElementById(self.getElementID());
                if(domElem) domElem.replaceNode(self.draw());
                
                // Signal associated slots
                self.signalSelect();
            };
            
            // Select cell style
            if(this.sdate != null && this.sdate.day == day && this.sdate.month == month && this.sdate.year == year){
                ret.setAttribute('class', 'gui_calendar_cell gui_calendar_selected');
            }else if(cdate.day == day && cdate.month == month && cdate.year == year){
                ret.setAttribute('class', 'gui_calendar_cell gui_calendar_today');
            }else if(this.checkMark(day, month, year) == true){
                ret.setAttribute('class', 'gui_calendar_cell gui_calendar_marked');
            }else{
                ret.setAttribute('class', 'gui_calendar_cell');
            }
            
        return ret;
    }
    
    // ********************************************************* //
    // ** PRIVATE :: SIGNALING METHODS                        ** //
    // ********************************************************* //
    
    this.signalSelect = function(){
        if(this.slotselect) this.slotselect(this.sdate);
    }
    
    // ********************************************************* //
    // ** PRIVATE :: AUXILIARY METHODS                        ** //
    // ********************************************************* //
    
    this.getElementID = function(){
        return 'gui_calendar_' + this.id;
    };
    
    this.checkMark = function(day, month, year){
        if(!this.marks[year]) return false;
        if(!this.marks[year][month]) return false;
        if(!this.marks[year][month][day]) return false;
        return true;
    };
    
    this.getMonthName = function(month){
        switch(month){
            case 1:return 'Gener';
            case 2:return 'Febrer';
            case 3:return 'Març';
            case 4:return 'Abril';
            case 5:return 'Maig';
            case 6:return 'Juny';
            case 7:return 'Juliol';
            case 8:return 'Agost';
            case 9:return 'Setembre';
            case 10:return 'Octubre';
            case 11:return 'Novembre';
            case 12:return 'Desembre';
        }
    };
    
    this.getMonthDays = function(year, month){
        var md;
        
        switch(month){
            case 1:md = 31;break;
            case 2:md = 28;break;
            case 3:md = 31;break;
            case 4:md = 30;break;
            case 5:md = 31;break;
            case 6:md = 30;break;
            case 7:md = 31;break;
            case 8:md = 31;break;
            case 9:md = 30;break;
            case 10:md = 31;break;
            case 11:md = 30;break;
            case 12:md = 31;break;
        }
        
        if(month == 2 && year % 4 == 0){
            if(year % 100 != 0 || year % 400 == 0){
                md++;
            }
        }
        
        return md;
    };
    
    this.getNextMonth = function(mark){
        var m = mark.month + 1;
        var y = mark.year;
        
        if(m>12){
            m = 1;
            y++;
        }
        
        return new TypeDate(1, m, y);
    };
    
    this.getPreviousMonth = function(mark){
        var m = mark.month - 1;
        var y = mark.year;
        
        if(m<1){
            m = 12;
            y--;
        }
        
        return new TypeDate(1, m, y);
    };
    
    // ********************************************************* //
    // ** CLASS CONSTRUCTOR                                   ** //
    // ********************************************************* //
    
    // Set current date, view date & selected date
    var d = new Date();
    this.cdate = new TypeDate(d.getDate(), d.getMonth()+1, d.getFullYear());
    this.vdate = this.cdate;
    this.sdate = this.cdate;
}