<?php
	abstract class DatabaseController{
	    private $hostname = null;
		private $username = null;
		private $database = null;
		
		// * **************************************************
		// * ** PUBLIC METHODS
		// * **************************************************
		
		public function __construct($hostname, $database, $username){
			$this->hostname = $hostname;
			$this->username = $username;
			$this->database = $database;
		}
		
		public function getHostname(){
			return $this->hostname;
		}
		
		public function getUsername(){
			return $this->username;
		}
		
		abstract public function __checkConnected();
		abstract public function query($request);
		abstract public function fetchRow($table);
		abstract public function escapeString($string);
	}
?>