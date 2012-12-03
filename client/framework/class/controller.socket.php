<?php
    class SocketController{
        static private $type_undef   = 0;
        static private $type_listen  = 1;
        static private $type_connec  = 2;
        
        private $connection;
        private $type;
        
        public function __construct(){
            $this->connection = NULL;
            $this->type = $type_undef;
        }
        
        //* ********************************************* *//
        //* ** PUBLIC ACCESS METHODS                   ** *//
        //* ********************************************* *//
        
        public function connect($address, $port){
            $fp = stream_socket_client('tcp://' . $address . ':' . $port, $errno, $errstr, 30);
            if(!$fp) return false;
                
            $this->connection = $fp;
            $this->type = $this->type_connec;
            
            return true;
        }
        
        public function close(){
            if($this->connection == NULL) return false;
            fclose($this->connection);
        }
        
        public function read(){
            if($this->connection == NULL) return false;
            $ret = "";
            
            while(!feof($this->connection)){
                $ret = $ret . fgets($this->connection, 1024);
            }
            
            return $ret;
        }
        
        public function write($data){
            if($this->connection == NULL) return false;
            fwrite($this->connection, $data);
        }
    }
?>
