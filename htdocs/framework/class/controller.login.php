<?php
	class LoginController{
		private $smarty;
		
		private $pars;
		
		public function __construct(){
			$this->pars = UtilsCore::extractUrlParameters();
		}
		
		public function run(){
			$this->smarty = SmartyView::getInstance();
			
			// If user already logged in, show error
			if(isSet($_SESSION['user'])){
				$error = ErrorController::getInstance();
				$error->run();
				return;
			}
			
			// Include JS and CSS
			$this->smarty->includeCSS('/css/login.css');
			$this->smarty->includeJS('/js/login.js');
			
			// Define language variables
			include(PATH_FWK_LANG.'login_'.LANGUAGE.'.php');
			$this->smarty->assign('login_title', LOGIN_TITLE);
			$this->smarty->assign('login_username', LOGIN_USERNAME);
			$this->smarty->assign('login_password', LOGIN_PASSWORD);
			$this->smarty->assign('login_button', LOGIN_BUTTON);
			$login = (isSet($this->pars[1]) && $this->pars[1] == 'login') ? true : false;
			$this->smarty->assign('login_exp_title', ($login) ? LOGIN_EXP_TITLE1 : LOGIN_EXP_TITLE2);
			$this->smarty->assign('login_exp_cont', ($login) ? LOGIN_EXP_CONT1 : LOGIN_EXP_CONT2);
			
			// Draw view
			$this->smarty->addTemplate('login.tpl');
		}
	}
?>