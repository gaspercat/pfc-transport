<?php
    class SocketDispatcher{
        static private $instance = null;
        private $socket;
        
        private function __construct(){
            $this->socket = array();
        }
        
        // * **************************************************
        // * ** PUBLIC METHODS
        // * **************************************************

        static public function getInstance(){
            if(self::$instance == null) self::$instance = new SocketDispatcher();
            return self::$instance;
        }
        
        public function getConnection($hostname, $ip){
            $hash = $this->getSocketHash($hostname, $ip);
            
            //Return opened socket if present 
            if(isSet($this->socket[$hash])){
                return $this->socket[$hash];
            }
            
            //Create new socket
            $socket = new SocketController();
            if($socket->connect($hostname, $ip) == true){
                $this->socket[$hash] = $socket;
                return $socket;
            }
            
            return false;
        }
        
        // * **************************************************
        // * ** PRIVATE METHODS
        // * **************************************************

        private function getSocketHash($hostname, $ip){
            $string = $hostname . $ip;
            $hash = hash('crc32', $string);
            return $hash;
        }
    }
?>
