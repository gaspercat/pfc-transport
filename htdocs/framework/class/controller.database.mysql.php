<?php
	/**
	 * Controller for the MySQL Database. It allows to create new connections, m
	 * ake queries, split tables into rows and escape strings
	 */
	class MysqlDatabaseController extends DatabaseController{
	    private $mysql = null;
		
		public function __construct($hostname, $database, $username, $password){
			parent::__construct($hostname, $database, $username);
			
			$mysql = mysql_connect($hostname, $username, $password);
			if($mysql != false){
				if(mysql_select_db($database, $mysql) == false){
					if(mysql_query('CREATE DATABASE ' . $database, $mysql) == false){
						return;
					}
					mysql_select_db($database, $mysql);
				}
				$this->mysql = $mysql;
			}
		}
		
		public function __checkConnected(){
			if($this->mysql == null) return false;
			return true;
		}
		
		// * **************************************************
		// * ** PUBLIC QUERY METHODS
		// * **************************************************
		
		public function query($request){
			if($this->mysql == null) return false;
			
		    $ret = mysql_query($request, $this->mysql);
			return $ret;
		}
		
		// * **************************************************
		// * ** PUBLIC THREATING METHODS
		// * **************************************************
		
		public function fetchRow($table){
			$ret = mysql_fetch_array($table);
			return $ret;
		}
		
		public function escapeString($string){
			$ret = htmlspecialchars($string);
			$ret = mysql_real_escape_string(stripslashes($ret));
			return $ret;
		}
	}
?>