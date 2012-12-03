<?php
    function __autoload($class){
        switch($class){
            case 'RouterCore':                  include_once(PATH_FWK.'core.router.php');                      break;
            case 'UtilsCore':                   include_once(PATH_FWK.'core.utils.php');                       break;
            // **************************** //
            case 'DatabaseDispatcher':          include_once(PATH_FWK_CLASS.'dispatcher.database.php');        break;
            case 'UserDispatcher':              include_once(PATH_FWK_CLASS.'dispatcher.user.php');            break;
            case 'SocketDispatcher':            include_once(PATH_FWK_CLASS.'dispatcher.socket.php');          break;
            // **************************** //
            case 'UserModel':                   include_once(PATH_FWK_CLASS.'model.user.php');                 break;
            // **************************** //
            case 'DatabaseController':          include_once(PATH_FWK_CLASS.'controller.database.php');        break;
            case 'MysqlDatabaseController':     include_once(PATH_FWK_CLASS.'controller.database.mysql.php');  break;
            case 'SocketController':            include_once(PATH_FWK_CLASS.'controller.socket.php');          break;
            case 'LoginController':             include_once(PATH_FWK_CLASS.'controller.login.php');           break;
            case 'RegisterController':          include_once(PATH_FWK_CLASS.'controller.register.php');        break;
            case 'ApplicationController':       include_once(PATH_FWK_CLASS.'controller.application.php');     break;
            case 'QueryController':             include_once(PATH_FWK_CLASS.'controller.query.php');           break;
            // **************************** //
            case 'SmartyView':                  include_once(PATH_FWK_CLASS.'view.smarty.php');                break;
            // **************************** //
            case 'SmartySL':                    include_once(PATH_FWK_LIB.'smarty/smarty.php');                break;
        }
    }
    spl_autoload_register('__autoload');

    class MvcCore{
        static private $instance = null;
        private $controller = null;

        private function __construct(){
            // Load configuration & constants
            include_once('config/configuration.php');
            include_once('config/constants.php');
            include_once('config/errors.php');
            
            // Resolve controller & language
            $router = RouterCore::getInstance();
            $this->controller = $router->resolveController();
            define('LANGUAGE', $router->resolveLanguage());

            // Start session
            $this->manageSession();
        }

        // * **************************************************
        // * ** PUBLIC METHODS
        // * **************************************************

        static public function getInstance(){
            if(self::$instance == null) self::$instance = new MvcCore();
            return self::$instance;
        }

        public function run(){
            $smarty = SmartyView::getInstance();

            // Load specific controller
            //try{
                $controller = new $this->controller();
                $controller->run();
            //}catch(Exception $e){
            //    $controller = new ErrorController();
            //	$controller->run(ERROR_APPLICATION_CRASH);
            //}

            //Render view
            $smarty->draw();
        }

        // * **************************************************
        // * ** SESSION MANAGEMENT
        // * **************************************************

        private function manageSession(){
            session_start();
            if(isSet($_GET['action'])){
                if($_GET['action'] == 'login'){
                    $this->loginUser($_POST['usr'], $_POST['pwd']);
                }else if($_GET['action'] == 'logout'){
                    $this->logoutUser();
                }
            }

        }

        private function loginUser($username, $password){
            // Check parameters are passed
            if(!isSet($username) || !isSet($password)){
                include_once(PATH_FWK_LANG.'login_'.LANGUAGE.'.php');
                echo '!'.LOGIN_EXCEPTION;
                die();
            }

            // Retrieve user
            $dispatcher = UserDispatcher::getInstance();
            $user = $dispatcher->getUserByName($username);
            if($user == false){
                include_once(PATH_FWK_LANG.'login_'.LANGUAGE.'.php');
                echo '!'.LOGIN_EXCEPTION;
                die();
            }

            // Check password
            if($user->validatePassword($password) == false){
                include_once(PATH_FWK_LANG.'login_'.LANGUAGE.'.php');
                echo '!'.LOGIN_EXCEPTION;
                die();
            }

            //Assign user to session
            $_SESSION['user'] = $user;
            echo '1';
            die();
        }

        private function logoutUser(){
            session_unset();
            session_destroy();
        }
    }
?>