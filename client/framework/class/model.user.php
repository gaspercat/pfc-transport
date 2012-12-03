<?php
	class UserModel{
		private $id = null;
		private $name = null;
		private $username = null;
		private $email = null;
		private $access = null;
		
		public function __construct($id, $name, $username, $email, $access = ACCESS_NONE){
			$this->id = $id;
			$this->name = $name;
			$this->username = $username;
			$this->email = $email;
			$this->access = $access;
		}
		
		//* ********************************************* *//
		//* ** PUBLIC ACCESS METHODS                   ** *//
		//* ********************************************* *//
		
		public function getID(){
			return $this->id;
		}
		
		public function getName(){
			return $this->name;
		}
		
		public function getUsername(){
			return $this->username;
		}
		
		public function getEmail(){
			return $this->email;
		}
		
		public function getAccess(){
			return $this->access;
		}
		
		//* ********************************************* *//
		//* ** PUBLIC EDITION METHODS                  ** *//
		//* ********************************************* *//
		
		public function setName($name){
			// Check name is valid
			if(!UtilsCore::checkName($name)) return false;
			
			// Get database connection
			$db_dispatcher = DatabaseDispatcher::getInstance();
			$db = $db_dispatcher->getConnection(DB_HOSTNAME, DB_DATABASE, DB_USERNAME, DB_PASSWORD);
			if($db == false) return false;
			
			// Update username
			$db->query("UPDATE users SET name='".$name."' WHERE id=".$this->id);
			$this->name = $name;
			
			return true;
		}
		
		public function setUsername($username){
			// Check username is valid
			if(!UtilsCore::checkUsername($username)) return false;
			
			// Get database connection
			$db_dispatcher = DatabaseDispatcher::getInstance();
			$db = $db_dispatcher->getConnection(DB_HOSTNAME, DB_DATABASE, DB_USERNAME, DB_PASSWORD);
			if($db == false) return false;
			
			// Update username
			$db->query("UPDATE users SET username='".$username."' WHERE id=".$this->id);
			$this->username = $username;
			
			return true;
		}
		
		public function setPassword($password){
			// Check username is valid
			if(!UtilsCore::checkPassword($password)) return false;
			
			// Get database connection
			$db_dispatcher = DatabaseDispatcher::getInstance();
			$db = $db_dispatcher->getConnection(DB_HOSTNAME, DB_DATABASE, DB_USERNAME, DB_PASSWORD);
			if($db == false) return false;
			
			// Update password
			$db->query("UPDATE users SET password='".$password."' WHERE id=".$this->id);
			$this->password = $password;
			
			return true;
		}
		
		public function setEmail($email){
			// Check username is valid
			if(!UtilsCore::checkEmail($email)) return false;
			
			// Get database connection
			$db_dispatcher = DatabaseDispatcher::getInstance();
			$db = $db_dispatcher->getConnection(DB_HOSTNAME, DB_DATABASE, DB_USERNAME, DB_PASSWORD);
			if($db == false) return false;
			
			// Update email
			$db->query("UPDATE users SET email='".$email."' WHERE id=".$this->id);
			$this->email = $email;
			
			return true;
		}
		
		public function setAccess($access){
			// Check username is valid
			if(!UtilsCore::checkAccess($access)) return false;
			
			// Get database connection
			$db_dispatcher = DatabaseDispatcher::getInstance();
			$db = $db_dispatcher->getConnection(DB_HOSTNAME, DB_DATABASE, DB_USERNAME, DB_PASSWORD);
			if($db == false) return false;
			
			// Update access
			$db->query("UPDATE users SET access=".$access." WHERE id=".$this->id);
			$this->access = $access;
			
			return true;
		}
		
		//* ********************************************* *//
		//* ** PUBLIC CHECKOUT METHODS                 ** *//
		//* ********************************************* *//
		
		public function validatePassword($password){
			// Check password is valid
			if(!UtilsCore::checkPassword($password)) return false;
			
			// Get database connection
			$db_dispatcher = DatabaseDispatcher::getInstance();
			$db = $db_dispatcher->getConnection(DB_HOSTNAME, DB_DATABASE, DB_USERNAME, DB_PASSWORD);
			if($db == false) return false;

			// Compare password
			$data = $db->query("SELECT COUNT(1) FROM users WHERE id=".$this->id." AND password='".$password."'");
			$data = $db->fetchRow($data);
			if($data == false) return false;
			if($data[0] == '0') return false;
			return true;
		}
	}
?>