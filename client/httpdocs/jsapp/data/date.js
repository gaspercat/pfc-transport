function TypeDate(day, month, year){
    this.day = day;
    this.month = month;
    this.year = year;
    
    this.copy = function(){
        return new TypeDate(this.day, this.month, this.year);
    };
    
    this.compareWith = function(date){
        if(this.year < date.year) return -1;
        if(this.year > date.year) return 1;
        if(this.month < date.month) return -1;
        if(this.month > date.month) return 1;
        if(this.day < date.day) return -1;
        if(this.day > date.day) return 1;
        return 0;
    };
    
    this.toLongString = function(){
        var ret = '';
        
        // Add day
        ret = ret + day + ' ';
        
        // Add month
        switch(month){
            case 1:   ret = ret + 'de Gener';      break;
            case 2:   ret = ret + 'de Febrer';     break;
            case 3:   ret = ret + 'de Març';       break;
            case 4:   ret = ret + 'd\'Abril';      break;
            case 5:   ret = ret + 'de Maig';       break;
            case 6:   ret = ret + 'de Juny';       break;
            case 7:   ret = ret + 'de Juliol';     break;
            case 8:   ret = ret + 'd\'Agost';      break;
            case 9:   ret = ret + 'de Setembre';   break;
            case 10:  ret = ret + 'd\'Octubre';    break;
            case 11:  ret = ret + 'de Novembre';   break;
            case 12:  ret = ret + 'de Desembre';   break;
        }
        
        // Add year
        ret = ret + ' de ' + year;
        
        return ret;
    };
}