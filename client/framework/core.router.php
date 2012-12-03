<?php
	class RouterCore{
		static private $instance = null;
		private $exploded = null;
		
		private function __construct(){
                    $this->exploded = explode('/', $_SERVER['REQUEST_URI']);
		}
		
		// * **************************************************
		// * ** PUBLIC METHODS
		// * **************************************************
		
		static public function getInstance(){
                    if(self::$instance == null) self::$instance = new RouterCore();
                    return self::$instance;
		}
		
		public function resolveController(){
                    if(!isSet($this->exploded[2])) return 'ApplicationController';

                    switch($this->exploded[2]){
                        case 'login':          return 'LoginController';
                        case 'register':       return 'RegisterController';
                        case 'application':    return 'ApplicationController';
                        case 'query':          return 'QueryController';
                    }
		}
		
		public function resolveLanguage(){
                    if(!isSet($this->exploded[1])) return 'en';

                    switch($this->exploded[1]){
                        case 'ca':             return 'ca';
                        case 'es':             return 'es';
                        case 'en':             return 'en';
                        default:               return 'en';
                    }
		}
	}
?>