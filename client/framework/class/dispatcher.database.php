<?php
	class DatabaseDispatcher{
		static private $instance = null;
		private $connection;
		
		private function __construct(){
			$this->connection = array();
		}
		
		// * **************************************************
		// * ** PUBLIC METHODS
		// * **************************************************
		
		static public function getInstance(){
			if(self::$instance == null) self::$instance = new DatabaseDispatcher();
			return self::$instance;
		}
		
		public function getConnection($hostname, $database, $username, $password){
		    $hash = $this->getConnectionHash($hostname, $database, $username);
			
			//Return opened connection if present 
			if(isSet($this->connection[$hash])){
				return $this->connection[$hash];
			} 
			
			//Create new connection
			$connection = new MysqlDatabaseController($hostname, $database, $username, $password);
			if($connection->__checkConnected() == true){
				$this->connection[$hash] = $connection;
				return $connection;
			}
			return false;
		}
		
		// * **************************************************
		// * ** PRIVATE METHODS
		// * **************************************************
		
		private function getConnectionHash($hostname, $database, $username){
		    $string = $hostname.$database.$username;
			$hash = hash('crc32', $string);
			return $hash;
		}
	}
?>