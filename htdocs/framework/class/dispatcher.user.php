<?php
	class UserDispatcher{
		static private $instance = null;
		private $user_byname = null;
		private $user_byid = null;
		
		private function __construct(){
                    $this->user_byname = array();
                    $this->user_byid = array();
		}
		
		//* ********************************************* *//
		//* ** PUBLIC METHODS                          ** *//
		//* ********************************************* *//
		
		static public function getInstance(){
                    if(self::$instance == null) self::$instance = new UserDispatcher();
                    return self::$instance;
		}
		
		public function getUserByName($username){
                    // Get user if on memory
                    if(isSet($this->user_byname[$username])){
                        return $this->user_byname[$username];
                    }

                    // Check username
                    if(UtilsCore::checkUsername($username) == false) return false;

                    // Get database connection
                    $db_dispatcher = DatabaseDispatcher::getInstance();
                    $db = $db_dispatcher->getConnection(DB_HOSTNAME, DB_DATABASE, DB_USERNAME, DB_PASSWORD);
                    if($db == false) return false;

                    // Get user data
                    $data = $db->query("SELECT id, name, username, email, access FROM users WHERE username='".$username."'");
                    if($data != false){
                        $data = $db->fetchRow($data);
                        if($data != false){
                            $ret = new UserModel($data['id'], $data['name'], $username, $data['email'], $data['access']);
                            $this->user_byname[$username] = $ret;
                            $this->user_byid[$data['id']] = $ret;
                            return $ret;
                        }
                    }
                    return false;
		}
		
		public function getUserByID($id){
                    // Get user if on memory
                    if(isSet($this->user_byid[$id])){
                        return $this->user_byid[$id];
                    }

                    //Check ID
                    if(UtilsCore::checkID($id) == false) return false;

                    // Get database connection
                    $db_dispatcher = DatabaseDispatcher::getInstance();
                    $db = $db_dispatcher->getConnection(DB_HOSTNAME, DB_DATABASE, DB_USERNAME, DB_PASSWORD);
                    if($db == false) return false;

                    // Get user data
                    $data = $db->query("SELECT id, name, username, email, access FROM users WHERE id=".$id);
                    if($data != false){
                        $data = $db->fetchRow($data);
                        if($data != false){
                            $ret = new UserModel($id, $data['name'], $data['username'], $data['email'], $data['access']);
                            $this->user_byname[$data['username']] = $ret;
                            $this->user_byid[$id] = $ret;
                            return $ret;
                        }
                    }
                    return false;
		}
		
		public function newUser($username, $password, $name, $email){
                    // Get database connection
                    $db_dispatcher = DatabaseDispatcher::getInstance();
                    $db = $db_dispatcher->getConnection(DB_HOSTNAME, DB_DATABASE, DB_USERNAME, DB_PASSWORD);
                    if($db == false) return false;

                    // Check parameters
                    if(UtilsCore::checkUsername($username) == false) return false;
                    if(UtilsCore::checkPassword($password) == false) return false;
                    if(UtilsCore::checkEmail($email) == false) return false;

                    // Check user doesn't exist
                    $data = $db->query("SELECT COUNT(1) FROM users WHERE username='".$username."'");
                    $data = $db->fetchRow($data);
                    if($data[0] != '0') return false;

                    // Create new user
                    $db->query("INSERT INTO users (username, password, name, email, access) VALUES ('".$username."', '".$password."', '".$name."', '".$email."', ".ACCESS_NONE.")");
                    $data = $db->query("SELECT id FROM users WHERE username='".$username."'");
                    $data = $db->fetchRow($data);
                    $ret = new UserModel($data['id'], $name, $username, $email, ACCESS_NONE);
                    $ret->setPassword($password);
                    return $ret;
		}
	}
?>