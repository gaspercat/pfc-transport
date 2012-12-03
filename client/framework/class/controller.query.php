<?php
    class QueryController{
        private $smarty;

        private $pars;
        private $sck;

        public function __construct(){
            $this->pars = UtilsCore::extractUrlParameters();
            
            // Open socket connection to Java Application
            $sk_dispatcher = SocketDispatcher::getInstance();
            $this->sck = $sk_dispatcher->getConnection("localhost", 3002);
            if($this->sck == false){
                $this->errorNoJavaConnection();
                die();
            }
        }

        public function run(){
            if(!isSet($_POST['req'])){
                $this->errorUnknown();
                die();
            }
            
            // Read request
            $raw = stripslashes($_POST['req']);
            $req = json_decode($raw);
            
            // Process depending on the request type
            switch($req->act){
                case "synu":    $this->requestSyncUp($req, $raw);   break;
                case "synd":    $this->requestSyncDown($req, $raw); break;
                default:        $this->errorInvalidRequest();                break;
            }
        }
        
        // ********************************************************* //
        // ** SPECIFIC REQUESTS PROCESSING                        ** //
        // ********************************************************* //
        
        private function requestSyncUp($req, $raw){
            switch($req->repo){
                case "users":       $this->requestSyncUpUsers($raw);         break;
                case "products":    $this->requestSyncUpProducts($raw);      break;
                case "drivers":     $this->requestSyncUpDrivers($raw);       break;
                case "transports":  $this->requestSyncUpTransports($raw);    break;
                case "depots":      $this->requestSyncUpDepots($raw);        break;
                case "clients":     $this->requestSyncUpClients($raw);       break;
            }
        }
        
        private function requestSyncDown($req, $raw){
            switch($req->repo){
                case "users":       $this->requestSyncDownUsers($raw);       break;
                case "products":    $this->requestSyncDownProducts($raw);    break;
                case "drivers":     $this->requestSyncDownDrivers($raw);     break;
                case "transports":  $this->requestSyncDownTransports($raw);  break;
                case "depots":      $this->requestSyncDownDepots($raw);      break;
                case "clients":     $this->requestSyncDownClients($raw);     break;
            }
        }
        
        // ********************************************************* //
        // ** SYNC-UP SPECIFIC METHODS                            ** //
        // ********************************************************* //
        
        private function requestSyncUpUsers($raw){
            // FIXME: CHECK PERMISSIONS
            // FIXME: MAKE REQUEST TO JAVA APPLICATION
        }
        
        private function requestSyncUpProducts($raw){
            // FIXME: CHECK PERMISSIONS
            // FIXME: MAKE REQUEST TO JAVA APPLICATION
        }
        
        private function requestSyncUpDrivers($raw){
            // FIXME: CHECK PERMISSIONS
            // FIXME: MAKE REQUEST TO JAVA APPLICATION
        }
        
        private function requestSyncUpTransports($raw){
            // FIXME: CHECK PERMISSIONS
            // FIXME: MAKE REQUEST TO JAVA APPLICATION
        }
        
        private function requestSyncUpDepots($raw){
            // FIXME: CHECK PERMISSIONS
            // FIXME: MAKE REQUEST TO JAVA APPLICATION
        }
        
        private function requestSyncUpClients($raw){
            // FIXME: CHECK PERMISSIONS
            // FIXME: MAKE REQUEST TO JAVA APPLICATION
        }
        
        // ********************************************************* //
        // ** SYNC-DOWN SPECIFIC METHODS                          ** //
        // ********************************************************* //
        
        private function requestSyncDownUsers($raw){
            // FIXME: CHECK PERMISSIONS
            // FIXME: MAKE REQUEST TO JAVA APPLICATION
        }
        
        private function requestSyncDownProducts($raw){
            // FIXME: CHECK PERMISSIONS
            // FIXME: MAKE REQUEST TO JAVA APPLICATION
            
            // Make request to Java Application
            $this->sck->write($raw);
            echo $this->sck->read();
            
            // FIXME: STUB!! GENERATE PRODUCTS
            /*echo('{"status":"ok","ver":2,"elems":[');
                echo('{"act":"add","id":1,"name":"object1","weight":100,"volume":15}');
                echo(',{"act":"add","id":2,"name":"object2","weight":100,"volume":15}');
                echo(',{"act":"add","id":3,"name":"object3","weight":100,"volume":15}');
                echo(',{"act":"add","id":4,"name":"object4","weight":100,"volume":15}');
                echo(',{"act":"add","id":5,"name":"object5","weight":100,"volume":15}');
                echo(',{"act":"add","id":6,"name":"object6","weight":100,"volume":15}');
                echo(',{"act":"add","id":7,"name":"object7","weight":100,"volume":15}');
                echo(',{"act":"add","id":8,"name":"object8","weight":100,"volume":15}');
            echo(']}');*/
        }
        
        private function requestSyncDownDrivers($raw){
            // FIXME: CHECK PERMISSIONS
            // FIXME: MAKE REQUEST TO JAVA APPLICATION
            
            // FIXME: STUB!! GENERATE PRODUCTS
            echo('{"status":"ok","ver":2,"elems":[');
            for($i=0;$i<20;$i++){
                echo('{"act":"add","id":' . ($i + 1) . ',"name":"conductor' . ($i + 1) . '","username":"uname","password":"pass","cellnum":666666666,"schedule":[');
                for($m=0;$m<12;$m++){
                    for($j=0;$j<15;$j++){
                        echo('{"year":2012,"month":' . ($m + 1) . ',"day":' . ($j + 1 + $i%7) . ',"sh":8,"sm":0,"eh":15,"em":45}');
                        if($j<15-1) echo(',');
                    }
                    if($m<12-1) echo(',');
                }
                echo(']}');
                if($i<20-1) echo(',');
            }
            echo(']}');
        }
        
        private function requestSyncDownTransports($raw){
            // FIXME: CHECK PERMISSIONS
            // FIXME: MAKE REQUEST TO JAVA APPLICATION
        }
        
        private function requestSyncDownDepots($raw){
            // FIXME: CHECK PERMISSIONS
            // FIXME: MAKE REQUEST TO JAVA APPLICATION
        }
        
        private function requestSyncDownClients($raw){
            // FIXME: CHECK PERMISSIONS
            // FIXME: MAKE REQUEST TO JAVA APPLICATION
        }
        
        // ********************************************************* //
        // ** ERROR RESPONSES                                     ** //
        // ********************************************************* //
        
        // Unknown error
        private function errorUnknown(){
            echo('{"status":"fail","code":1}');
        }
        
        // No Java connection
        private function errorNoJavaConnection(){
            echo('{"status":"fail","code":2}');
        }
        
        // Invalid request
        private function errorInvalidRequest(){
            echo('{"status":"fail","code":3}');
        }
    }
?>