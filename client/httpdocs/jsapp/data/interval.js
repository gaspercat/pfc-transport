function TypeInterval(/* [start, end] || [stHour, stMinute, edHour, edMinute] */){
    this.start;
    this.end;
    
    // ********************************************************* //
    // ** Getters                                             ** //
    // ********************************************************* //
    
    this.stHour = function(){
        return this.start.hour;
    };
    
    this.stMinute = function(){
        return this.start.minute;
    };
    
    this.edHour = function(){
        return this.end.hour;
    };
    
    this.edMinute = function(){
        return this.end.minute;
    };
    
    this.copy = function(){
        return new TypeInterval(this.start, this.end);
    };
    
    this.overlapsWith = function(ival){
        if(this.start.compareWith(ival.start) <= 0){
            if(this.end.compareWith(ival.start) >= 0){
                return true;
            }else{
                return false;
            }
        }else{
            if(this.start.compareWith(ival.end) <= 0){
                return true;
            }else{
                return false;
            }
        }
    };
    
    this.mergeWith = function(ival){
        if(this.start.compareWith(ival.start) <= 0){
            if(this.end.compareWith(ival.end) >= 0){
                return new TypeInterval(this.start, this.end);
            }else{
                return new TypeInterval(this.start, ival.end);
            }
        }else{
            if(this.end.compareWith(ival.end) >= 0){
                return new TypeInterval(ival.start, this.end);
            }else{
                return new TypeInterval(ival.start, ival.end);
            }
        }
    };
    
    // ********************************************************* //
    // ** CLASS CONSTRUCTOR                                   ** //
    // ********************************************************* //
    
    // If arguments are two TypeTime elements
    if(arguments.length == 2){
        this.start  = arguments[0].copy();
        this.end    = arguments[1].copy();
    
    // If arguments are two pairs of hour and minute
    }else if(arguments.length == 4){
        this.start  = new TypeTime(arguments[0], arguments[1]);
        this.end    = new TypeTime(arguments[2], arguments[3]);
    }
}