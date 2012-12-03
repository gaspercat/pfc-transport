<?php
	class ErrorController{
		static private $instance = null;
		
		private function __construct(){
			
		}
		
		// * **************************************************
		// * ** PUBLIC METHODS
		// * **************************************************
		
		static public function getInstance(){
			if(self::$instance == null) self::$instance = new ErrorController();
			return self::$instance;
		}
		
		public function run($code){
			// Load error strings
			include(PATH_FWK_LANG.'error_'.LANGUAGE.'.php');
			
			$smarty = new SmartySL();
			$smarty->assign('err_title', $this->getErrorTitle($code));
			$smarty->assign('err_desc', $this->getErrorDescription($code));
			$smarty->display(PATH_FWK_TEMPLATE.'error.tpl');
		}
		
		public function getErrorTitle($code){
			switch($code){
				case ERROR_PAGE_DOESNT_EXIST:     return ERROR_TITLE_PAGE_DOESNT_EXIST;
				case ERROR_NO_DB_CONNECTION:      return ERROR_TITLE_NO_DB_CONNECTION;
				case ERROR_APPLICATION_CRASH:     return ERROR_TITLE_APPLICATION_CRASH;
				case ERROR_UNKNOWN:               return ERROR_TITLE_UNKNOWN;
			}
			return ERROR_DESC_UNKNOWN;
		}
		
		public function getErrorDescription($code){
			switch($code){
				case ERROR_PAGE_DOESNT_EXIST:     return ERROR_DESC_PAGE_DOESNT_EXIST;
				case ERROR_NO_DB_CONNECTION:      return ERROR_DESC_NO_DB_CONNECTION;
				case ERROR_APPLICATION_CRASH:     return ERROR_DESC_APPLICATION_CRASH;
				case ERROR_UNKNOWN:               return ERROR_DESC_UNKNOWN;
			}
			return ERROR_DESC_UNKNOWN;
		}
	}
?>