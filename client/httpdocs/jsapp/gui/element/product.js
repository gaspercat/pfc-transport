function gui_element_product(id, name, volume, weight){
    this.id = id;
    this.name = name;
    this.volume = volume;
    this.weight = weight;
    
    // Edition DOM elements
    this.fName = new gui_field_text();
    this.fName.setValue(name);
    this.fWeight = new gui_field_numeric();
    this.fWeight.setValue(weight);
    this.fVolume = new gui_field_numeric();
    this.fVolume.setValue(volume);
    
    // ********************************************************* //
    // ** PUBLIC :: GETTERS                                   ** //
    // ********************************************************* //
    
    this.getID = function(){
        return this.id;
    };
    
    this.getName = function(){
        return this.name;
    };
    
    this.getVolume = function(){
        return this.volume;
    };
    
    this.getWeight = function(){
        return this.weight;
    };
    
    // ********************************************************* //
    // ** PUBLIC :: SETTERS                                   ** //
    // ********************************************************* //
    
    this.setWeight = function(weight){
        this.weight = weight;
        this.fWeight.setValue(weight);
    };
    
    this.setVolume = function(volume){
        this.volume = volume;
        this.fVolume.setValue(volume);
    };
    
    this.setFocus = function(){
        this.fName.setFocus();
    };
    
    // ********************************************************* //
    // ** PUBLIC :: ACTUATORS                                 ** //
    // ********************************************************* //
    
    /**
     * Check if the update form parameters are correctly formatted
     * @return true if valid, an error message if invalid
     */
    this.checkUpdateForm = function(){
        var name = this.fName.getValue();
        var volume = this.fVolume.getValue();
        var weight = this.fWeight.getValue();
        
        // Check name isn't empty
        if(name.length == 0){
            return "El nom no pot ser un camp buit";
        }
        
        // Check name isn't composed only by spaces
        var n = 0;
        for(var i=0;i<name.length;i++){
            if(name.charAt(i) != ' ') n++;
        }
        if(n == 0) return "El nom ha de contenir com a m&iacute;nim car&agrave;cter alfanum&egrave;ric";

        // Return true upon success
        return true;
    };
    
    this.updateFromForm = function(){
        this.name = this.fName.getValue();
        this.volume = this.fVolume.getValue();
        this.weight = this.fWeight.getValue();
    };
    
    this.jsonStructure = function(){
        var ret = new Object();
        
        ret['id'] = this.id;
        ret['name'] = this.name;
        ret['volume'] = this.volume;
        ret['weight'] = this.weight;
        
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
                    td.appendChild(document.createTextNode('Volum:')); 
                tr.appendChild(td);
            
                td = document.createElement('td');
                    td.style.textAlign = 'left';
                    td.style.paddingLeft = '10px';
                    td.style.paddingTop = '4px';
                    td.appendChild(this.fVolume.draw());
                    td.appendChild(document.createTextNode('m3'));
                tr.appendChild(td);
            
            ret.appendChild(tr);
            
            tr = document.createElement('tr');
                td = document.createElement('td');
                    td.style.height = '24px';
                    td.style.paddingTop = '4px';
                    td.style.textAlign = 'right';  
                    td.appendChild(document.createTextNode('Pes:')); 
                tr.appendChild(td);
            
                td = document.createElement('td');
                    td.style.textAlign = 'left';
                    td.style.paddingLeft = '10px';
                    td.style.paddingTop = '4px';
                    td.appendChild(this.fWeight.draw());
                    td.appendChild(document.createTextNode('kg'));
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
                    td.appendChild(document.createTextNode('Volum'));
                tr.appendChild(td);
                td = document.createElement('td');
                    td.style.paddingLeft = '15px';
                    td.style.fontSize = '14px';
                    td.appendChild(document.createTextNode(this.volume + 'm3'));
                tr.appendChild(td);
            ret.appendChild(tr);
            
            tr = document.createElement('tr');
                td = document.createElement('td');
                    td.style.paddingLeft = '15px';
                    td.style.height = '18px';
                    td.style.fontSize = '14px';
                    td.appendChild(document.createTextNode('Pes'));
                tr.appendChild(td);
                td = document.createElement('td');
                    td.style.paddingLeft = '15px';
                    td.style.fontSize = '14px';
                    td.appendChild(document.createTextNode(this.weight + 'kg'));
                tr.appendChild(td);
            ret.appendChild(tr);
                
        return ret;
    };
}

function newProductFromJson(json){
    var product = new gui_element_product(json['id'], json['name'], json['volume'], json['weight']);
    return product;
}