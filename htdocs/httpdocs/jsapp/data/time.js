function TypeTime(hour, minute){
    this.hour = hour;
    this.minute = minute;
    
    this.copy = function(){
        return new TypeTime(this.hour, this.minute);
    };
    
    this.toString = function(){
        var ret = (this.hour < 10) ? '0' + this.hour : this.hour;
        return ret + ':' + ((this.minute < 10) ? '0' + this.minute : this.minute);
    };
    
    this.compareWith = function(time){
        if(this.hour < time.hour) return -1;
        if(this.hour > time.hour) return 1;
        if(this.minute < time.minute) return -1;
        if(this.minute > time.minute) return 1;
        return 0;
    };
}