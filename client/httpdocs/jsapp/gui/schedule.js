gui_schedule_count = 0;

function gui_schedule(){
    this.id = gui_schedule_count;
    gui_schedule_count++;
    
    // Positioning attributes
    this.left = 0;
    this.top = 0;
    this.width = 400;
    this.height = 100;
    
    // Content attributes
    this.title = '';
    this.intervals = new Array();
    this.clipboard = null;
    
    // Edition DOM elements
    this.fMaxtime = new gui_field_time();
    this.fMaxtime.setValue(new TypeTime(8,0));
    
    // Slots for list signals
    this.slotselect = null;
    
    // Auxiliary attributes
    this.selecting = false;
    
    // ********************************************************* //
    // ** PUBLIC :: GETTERS                                   ** //
    // ********************************************************* //
    
    this.getID = function(){
        return this.id;
    };
    
    this.getDomID = function(){
        return this.getElementID();
    };
    
    this.getLeft = function(){
        return this.left;
    };
    
    this.getTop = function(){
        return this.top;
    };
    
    this.getWidth = function(){
        return this.width;
    };
    
    this.getHeight = function(){
        return this.hegiht;
    };
    
    this.getIntervals = function(){
        var ret = new Array();
        
        for(var i=0;i<this.intervals.length;i++){
            ret[ret.length] = this.intervals[i].copy();
        }
        
        return ret;
    };
    
    this.getMaxTime = function(){
        return this.fMaxTime.getValue();
    };
    
    // ********************************************************* //
    // ** PUBLIC :: SETTERS                                   ** //
    // ********************************************************* //
    
    this.setLeft = function(left){
        this.left = left;
    };
    
    this.setTop = function(top){
        this.tiop = top;
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
    
    this.setTitle = function(title){
        this.title = title;
    };
    
    // ********************************************************* //
    // ** PUBLIC :: SIGNALS MANAGEMENT                        ** //
    // ********************************************************* //
    
    this.setSelectSlot = function(funct){
        this.slotselect = funct;
    }
    
    // ********************************************************* //
    // ** PUBLIC :: CONTENT MANAGEMENT                        ** //
    // ********************************************************* //
    
    this.clear = function(){
        // Remove marks
        this.intervals = new Array();
        
        // Redraw calendar
        var domElem = document.getElementById(this.getElementID());
        if(domElem) domElem.replaceNode(this.draw())
    };
    
    this.addInterval = function(/* [start, end] || [sh, sm, eh, em] */){
        var ival;
        
        // Construct interval from two times
        if(arguments.length == 2){
            if(arguments[0].compareWith(arguments[1]) == 0) return;
            ival = new TypeInterval(arguments[0], arguments[1]);
            
        // Construct interval from two pairs of hour and minute
        }else if(arguments.length == 4){
            if(arguments[0] == arguments[2] && arguments[1] == arguments[3]) return;
            ival = new TypeInterval(arguments[0], arguments[1], arguments[2], arguments[3]);
        }
        
        // Merge overlaping intervals
        for(var i=this.intervals.length-1;i>=0;i--){
            if(ival.overlapsWith(this.intervals[i])){
                ival = ival.mergeWith(this.intervals[i]);
                this.intervals.splice(i, 1);
            }
        }
        
        // Add new interval
        this.intervals[this.intervals.length] = ival;
        
        // Redraw schedule
        var domElem = document.getElementById(this.getElementID());
        if(domElem) domElem.replaceNode(this.draw());
    };
    
    // ********************************************************* //
    // ** PUBLIC :: DRAWING METHODS                           ** //
    // ********************************************************* //
    
    this.draw = function(){
        var self = this;
        
        // Events control
        this.selecting = false;
        
        var ret = document.createElement('div');
            ret.setAttribute('id', this.getElementID());
            ret.style.position = 'absolute';
            ret.style.left = this.left + 'px';
            ret.style.top = this.top + 'px';
            ret.style.width = (this.width - 4) + 'px';
            ret.style.height = (this.height - 4) + 'px';
            ret.style.border = '2px dashed #EEE';
            
            // Draw title
            var ttl = document.createElement('div');
                ttl.style.position = 'absolute';
                ttl.style.left = '10px';
                ttl.style.top = '10px';
                ttl.style.textAlign = 'left';
                ttl.style.fontSize = '18px';
                ttl.appendChild(document.createTextNode(this.title));
            ret.appendChild(ttl);
            
            // Draw bar
            var bar = document.createElement('div');
                bar.setAttribute('id', this.getElementID() + '_bar');
                bar.style.position = 'absolute';
                bar.style.left = '20px';
                bar.style.top = ((this.height - 30) / 2) + 'px';
                bar.style.height = '4px';
                bar.style.width = (this.width - 44) + 'px';
                bar.style.backgroundColor = '#CCC';
            ret.appendChild(bar);
            
            // Draw intervals
            for(var i=0;i<this.intervals.length;i++){
                var ival = this.intervals[i];
                
                // Get times and bar positions
                var stime = ival.start.copy();
                var etime = ival.end.copy();
                var spos = this.getTimePosition(bar, stime);
                var epos = this.getTimePosition(bar, etime);
                
                // Draw start mark
                var smark = this.drawStartMark(stime, '#DDD');
                    smark.style.left = (spos + 20 - 22) + 'px';
                ret.appendChild(smark);
                
                // Draw end mark
                var emark = this.drawEndMark(etime, '#DDD');
                    emark.style.left = (epos + 20 - 22) + 'px';
                ret.appendChild(emark);
                
                // Draw bar
                var cbar = document.createElement('div');
                    cbar.style.position = 'absolute';
                    cbar.style.height = '4px';
                    cbar.style.width = (epos - spos) + 'px';
                    cbar.style.top = ((this.height - 30) / 2) + 'px';
                    cbar.style.left = (spos + 20) + 'px';
                    cbar.style.backgroundColor = '#F99';
                ret.appendChild(cbar);
                
                // make markers able to move
                this.displaceMarkers(ival, bar, cbar, smark, emark);
            }
            
            // Draw selection surface
            var sel = document.createElement('div');
                sel.style.position = 'absolute';
                sel.style.zIndex = '2';
                sel.style.left = '18px';
                sel.style.top = ((this.height - 56) / 2) + 'px';
                sel.style.height = '30px';
                sel.style.width = (this.width - 40) + 'px';
                sel.style.cursor = 'pointer';

                // Interval selection events
                sel.onmousedown = function(){
                    // Deactivate time preview
                    sel.onmouseout();
                    sel.onmousemove = null;
                    sel.onmouseout = null;
                    
                    // Activate selection action
                    self.selecting = true;
                    
                    // Remove time preview mark if present
                    var domElem = document.getElementById(self.getElementID() + '_init');
                    if(domElem) domElem.removeNode();
                    
                    // Get mouse position
                    var mpos = getMouseX(arguments[0] || window.event);
                    
                    // Define start and end times
                    var stime = self.getMouseTime(bar, mpos);
                    var etime = stime;
                    
                    // Define start and end positions
                    var spos = self.getTimePosition(bar, stime);
                    var epos = spos;
                    
                    // Draw start mark
                    var smark = self.drawStartMark(stime, '#DDD');
                        smark.style.left = (spos + 20 - 22) + 'px';
                    ret.appendChild(smark);
                    
                    // Draw end mark
                    var emark = self.drawEndMark(stime, '#DDD');
                        emark.style.left = (epos + 20 - 22) + 'px';
                    ret.appendChild(emark);
                    
                    var cbar = document.createElement('div');
                        cbar.style.position = 'absolute';
                        cbar.style.height = '4px';
                        cbar.style.width = '0px';
                        cbar.style.top = ((self.height - 30) / 2) + 'px';
                        cbar.style.left = (spos + 20) + 'px';
                        cbar.style.backgroundColor = '#F99';
                    ret.appendChild(cbar);
                    
                    ret.onmousemove = function(){
                        // Get time on mouse position
                        var mpos = getMouseX(arguments[0] || window.event);
                        etime = self.getMouseTime(bar, mpos);
                        
                        // Calculate time positions
                        spos = self.getTimePosition(bar, stime);
                        epos = self.getTimePosition(bar, etime)
                        
                        // Move marks accordingly
                        if(stime.compareWith(etime) <= 0){
                            smark.style.left = (spos + 20 - 22) + 'px';
                            smark.setTime(stime);
                            emark.style.left = (epos + 20 - 22) + 'px';
                            emark.setTime(etime);
                            cbar.style.left = (spos + 20) + 'px';
                            cbar.style.width = (epos - spos) + 'px';
                        }else{
                            smark.style.left = (epos + 20 - 22) + 'px';
                            smark.setTime(etime);
                            emark.style.left = (spos + 20 - 22) + 'px';
                            emark.setTime(stime);
                            cbar.style.left = (epos + 20) + 'px';
                            cbar.style.width = (spos - epos) + 'px';
                        }
                    };
                    
                    // Set event for mouse up
                    ret.onmouseup = function(){
                        // Deactivate mouse move event
                        ret.onmousemove = null;
                        
                        // Desactivate selection action
                        self.selecting = false;
                        
                        // Add new interval
                        if(stime.compareWith(etime) <= 0){
                            self.addInterval(stime.hour, stime.minute, etime.hour, etime.minute);
                        }else{
                            self.addInterval(etime.hour, etime.minute, stime.hour, stime.minute);
                        }
                        
                        // Redraw schedule
                        var domElem = document.getElementById(self.getElementID());
                        if(domElem) domElem.replaceNode(self.draw());
                        
                        // Alert interval selection
                        self.signalSelect();
                    };
                };
                
                // Time preview events
                sel.onmouseover = function(){
                    if(self.selecting == true) return;
                    
                    // Get time on mouse position & bar position
                    var mpos = getMouseX(arguments[0] || window.event);
                    stime = self.getMouseTime(bar, mpos);
                    spos = self.getTimePosition(bar, stime);
                        
                    // Draw preview mark
                    var smark = self.drawStartMark(stime, '#DDD');
                        smark.style.zIndex = '1';
                        smark.style.left = (spos + 20 - 22) + 'px';
                    ret.appendChild(smark);
                    
                    sel.onmousemove = function(){
                        // Get time on mouse position & bar position
                        mpos = getMouseX(arguments[0] || window.event);
                        stime = self.getMouseTime(bar, mpos);
                        spos = self.getTimePosition(bar, stime);

                        // Position mark
                        smark.style.left = (spos + 20 - 22) + 'px';
                        smark.setTime(stime);
                    };

                    sel.onmouseout = function(){
                        // Disable onmousemove & onmouseout
                        sel.onmousemove = null;
                        sel.onmouseout = null;
                        
                        // Remove preview mark
                        smark.removeNode();
                    };
                };
                
            ret.appendChild(sel);
            
            // Draw maxtime label
            var mlb = document.createElement('div');
                mlb.style.position = 'absolute';
                mlb.style.width = '300px';
                mlb.style.height = '20px';
                mlb.style.left = ((this.width - 300) / 2) + 'px';
                mlb.style.top = (this.height - 65) + 'px';
                mlb.style.textAlign = 'center';
                mlb.appendChild(document.createTextNode('Màxim d\'hores'));
            ret.appendChild(mlb);
            
            // Draw maxtime field
            var mtm = this.fMaxtime.draw();
                mtm.style.border = '1px solid #CCC';
                mtm.style.position = 'absolute';
                mtm.style.left = ((this.width - this.fMaxtime.getWidth()) / 2) + 'px';
                mtm.style.top = (this.height - 40) + 'px';
            ret.appendChild(mtm);
            
            // Draw copy button
            var cpy = document.createElement('div');
                cpy.setAttribute('class', 'gui_schedule_copy');
                cpy.style.position = 'absolute';
                cpy.style.left = (this.width - 34) + 'px';
                cpy.style.top = (this.height - 34) + 'px';
                
                cpy.onclick = function(){
                    self.toClipboard();
                }
                
            ret.appendChild(cpy);
            
            // Draw paste button
            var pte = document.createElement('div');
                pte.setAttribute('class', 'gui_schedule_paste');
                pte.style.position = 'absolute';
                pte.style.left = (this.width - 64) + 'px';
                pte.style.top = (this.height - 34) + 'px';
                
                pte.onclick = function(){
                    self.fromClipboard();
                }
                
            ret.appendChild(pte);
        
        return ret;
    };
    
    // ********************************************************* //
    // ** PRIVATE :: DRAWING METHODS                          ** //
    // ********************************************************* //
    
    this.drawStartMark = function(time, color){
        var ret = document.createElement('div');
            ret.style.position = 'absolute';
            ret.style.top = (this.height / 2 - 49) + 'px';
            ret.style.width = '45px';
            ret.style.height = '35px';
            
            var thr = (time.hour < 10) ? '0' + time.hour : time.hour;
            var tmn = (time.minute < 10) ? '0' + time.minute : time.minute;
            
            // Draw label
            var lbl = document.createElement('div');
                lbl.setAttribute('class', 'gui_schedule_tag');
                lbl.style.position = 'absolute';
                lbl.style.left = '0px';
                lbl.style.top = '1px';
                lbl.style.width = '100%';
                lbl.style.height = '17px';
                lbl.style.backgroundColor = color;
                lbl.style.color = '#000';
                lbl.style.textAlign = 'center';
                lbl.style.fontSize = '15px';
                lbl.style.paddingTop = '2px';
            ret.appendChild(lbl);
            
            // Draw arrow
            var tri = document.createElement('div');
                tri.style.position = 'absolute';
                tri.style.left = '15px';
                tri.style.top = '20px';
                tri.style.width = '0px';
                tri.style.height = '0px';
                tri.style.width = '0px';
                tri.style.height = '0px';
                tri.style.borderTop = '15px solid ' + color;
                tri.style.borderLeft = '8px solid transparent';
                tri.style.borderRight = '8px solid transparent';
            ret.appendChild(tri);
            
            ret.setTime = function(time){
                var thr = (time.hour < 10) ? '0' + time.hour : time.hour;
                var tmn = (time.minute < 10) ? '0' + time.minute : time.minute;
                
                if(lbl.childNodes[0]) lbl.childNodes[0].removeNode();
                lbl.appendChild(document.createTextNode(thr + ':' + tmn));
            };
            
            ret.setTime(time);
        
        return ret;
    }
    
    this.drawEndMark = function(time, color){
        var ret = document.createElement('div');
            ret.style.position = 'absolute';
            ret.style.top = ((this.height - 25) / 2) + 'px';
            ret.style.width = '45px';
            ret.style.height = '35px';
            
            var thr = (time.hour < 10) ? '0' + time.hour : time.hour;
            var tmn = (time.minute < 10) ? '0' + time.minute : time.minute;
            
            // Draw label
            var lbl = document.createElement('div');
                lbl.setAttribute('class', 'gui_schedule_tag');
                lbl.style.position = 'absolute';
                lbl.style.left = '0px';
                lbl.style.top = '15px';
                lbl.style.width = '100%';
                lbl.style.height = '17px';
                lbl.style.backgroundColor = color;
                lbl.style.color = '#000';
                lbl.style.textAlign = 'center';
                lbl.style.fontSize = '15px';
                lbl.style.paddingTop = '2px';
            ret.appendChild(lbl);
            
            // Draw arrow
            var tri = document.createElement('div');
                tri.style.position = 'absolute';
                tri.style.left = '15px';
                tri.style.top = '0px';
                tri.style.width = '0px';
                tri.style.height = '0px';
                tri.style.width = '0px';
                tri.style.height = '0px';
                tri.style.borderBottom = '15px solid ' + color;
                tri.style.borderLeft = '8px solid transparent';
                tri.style.borderRight = '8px solid transparent';
            ret.appendChild(tri);
            
            ret.setTime = function(time){
                var thr = (time.hour < 10) ? '0' + time.hour : time.hour;
                var tmn = (time.minute < 10) ? '0' + time.minute : time.minute;
                
                if(lbl.childNodes[0]) lbl.childNodes[0].removeNode();
                lbl.appendChild(document.createTextNode(thr + ':' + tmn));
            };
            
            ret.setTime(time);
        
        return ret;
    };
    
    // ********************************************************* //
    // ** PRIVATE :: SIGNALING METHODS                        ** //
    // ********************************************************* //
    
    this.signalSelect = function(){
        if(this.slotselect) this.slotselect();
    }
    
    // ********************************************************* //
    // ** PRIVATE :: AUXILIARY METHODS                        ** //
    // ********************************************************* //
    
    this.getElementID = function(){
        return 'gui_schedule_' + this.id;
    };
    
    this.getMouseTime = function(bar, x){
        // Get coordinates for 00:00 & 24:00
        var xLeft = bar.getPositionX();
        var xRight = xLeft + parseInt(bar.style.width);
        
        // Get mouse position as a fraction of 2400
        if(x<xLeft)   x = xLeft;
        if(x>xRight)  x = xRight;
        var ptd = (x - xLeft) * 2400 / (xRight - xLeft);
        
        // Get hour and minute
        var h = parseInt(ptd/100);
        var m = parseInt(((ptd % 100) * 60 / 100) / 5) * 5;
        
        return new TypeTime(h, m);
    };
    
    this.getTimePosition = function(bar, time){
        var barWidth = parseInt(bar.style.width);
        var ptd = time.hour * 100 + time.minute * 100 / 60;
        return barWidth * ptd / 2400;
    };
    
    this.displaceMarkers = function(ival, bar, cbar, smark, emark){
        var self = this;
        
        // Get times and bar positions
        var stime = ival.start.copy();
        var etime = ival.end.copy();
        var spos = this.getTimePosition(bar, stime);
        var epos = this.getTimePosition(bar, etime);
        
        smark.onmousedown = function(){
            // Disable time preview
            self.selecting = true;
            
            // Get mouse position
            var mpos = getMouseX(arguments[0] || window.event);

            // Define start time & position
            stime = self.getMouseTime(bar, mpos);
            spos = self.getTimePosition(bar, stime);

            // Marker displacement
            smark.parentNode.onmousemove = function(){
                // Get mouse position
                var mpos = getMouseX(arguments[0] || window.event);

                // Calculate time
                stime = self.getMouseTime(bar, mpos);
                if(stime.compareWith(etime) > 0){
                    stime.hour = etime.hour;
                    stime.minute = etime.minute;
                }
                
                // Calculate time position
                spos = self.getTimePosition(bar, stime);

                // Move mark accordingly
                smark.style.left = (spos + 20 - 22) + 'px';
                smark.setTime(stime);
                cbar.style.left = (spos + 20) + 'px';
                cbar.style.width = (epos - spos) + 'px';
            };

            smark.parentNode.onmouseup = function(){
                // Deactivate mouse events
                smark.parentNode.onmousemove = null;
                smark.parentNode.onmouseup = null;

                // Replace old interval with new one
                self.intervals.remove(ival);
                self.addInterval(stime.hour, stime.minute, etime.hour, etime.minute);

                // Enable time preview
                self.selecting = false;

                // Redraw schedule
                var domElem = document.getElementById(self.getElementID());
                if(domElem) domElem.replaceNode(self.draw());
                
                // Alert interval selection
                self.signalSelect();
            };
        };
        
        emark.onmousedown = function(){
            // Disable time preview
            self.selecting = true;
            
            // Get mouse position
            var mpos = getMouseX(arguments[0] || window.event);

            // Define start time & position
            etime = self.getMouseTime(bar, mpos);
            epos = self.getTimePosition(bar, etime);

            // Marker displacement
            emark.parentNode.onmousemove = function(){
                // Get mouse position
                var mpos = getMouseX(arguments[0] || window.event);

                // Calculate time
                etime = self.getMouseTime(bar, mpos);
                if(etime.compareWith(stime) < 0){
                    etime.hour = stime.hour;
                    etime.minute = stime.minute;
                }
                
                // Calculate time position
                epos = self.getTimePosition(bar, etime);

                // Move mark accordingly
                emark.style.left = (epos + 20 - 22) + 'px';
                emark.setTime(etime);
                cbar.style.left = (spos + 20) + 'px';
                cbar.style.width = (epos - spos) + 'px';
            };

            emark.parentNode.onmouseup = function(){
                // Deactivate mouse events
                emark.parentNode.onmousemove = null;
                emark.parentNode.onmouseup = null;

                // Replace old interval with new one
                self.intervals.remove(ival);
                self.addInterval(stime.hour, stime.minute, etime.hour, etime.minute);

                // Enable time preview
                self.selecting = false;

                // Redraw schedule
                var domElem = document.getElementById(self.getElementID());
                if(domElem) domElem.replaceNode(self.draw());
                
                // Alert interval selection
                self.signalSelect();
            };
        };
    };
    
    this.toClipboard = function(){
        // Copy intervals to clipboard
        this.clipboard = new Array();
        for(var i=0;i<this.intervals.length;i++){
            this.clipboard[i] = this.intervals[i].copy();
        };
    };
    
    this.fromClipboard = function(){
        if(this.clipboard == null) return;
        
        // Copy intervals from clipboard
        this.intervals = new Array();
        for(var i=0;i<this.clipboard.length;i++){
            this.intervals[i] = this.clipboard[i].copy();
        };
        
        // Redraw schedule
        var domElem = document.getElementById(this.getElementID());
        if(domElem) domElem.replaceNode(this.draw());

        // Alert interval selection
        this.signalSelect();
    };
}