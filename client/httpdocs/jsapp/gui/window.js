var gui_window_count = 0;

function gui_window(){
    this.id = gui_window_count;
    this.content = null;
    
    this.left = 100;
    this.top = 100;
    
    this.width = 400;
    this.height = 300;
    
    gui_window_count++;
    
    this.getID = function(){
        return this.id;
    };
    
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
    
    this.setContent = function(content){
        this.content = content;
    };
    
    this.draw = function(){
        var ret = document.createElement('div');
            ret.setAttribute('id', 'gui_container_' + this.id);
            ret.style.width = this.width;
            ret.style.height = this.height;
            ret.style.position = 'absolute';
            ret.style.left = this.left + 'px';
            ret.style.top = this.top + 'px';
            ret.style.width = (this.width == -1) ? '100%' : this.width + 'px';
            ret.style.height = (this.height == -1) ? '100%' : this.height + 'px';
            
            var tab = document.createElement('table');
                tab.style.borderCollapse = 'collapse';
                tab.style.width = '100%';
                tab.style.height = '100%';
                var tr = document.createElement('tr');
            
                for(var i=0;i<this.content.length;i++){
                    var td = document.createElement('td');
                        if(this.content[i].getWidth() != -1){
                            td.style.width = this.content[i].getWidth();
                        }
                        td.appendChild(this.content[i].draw());
                    tr.appendChild(td);
                }
                
                tab.appendChild(tr);
            ret.appendChild(tab);
            
        return ret;
    };
}