<?php
    class SmartyView{
        static private $instance;

        private $smarty;
        private $templates;
        private $includes_js;
        private $includes_css;

        private function __construct(){
            //Initialize smarty
            $this->smarty = new SmartySL();

            //Get current URL
            $currpage = $_SERVER['REDIRECT_URL'];
            if($currpage[strlen($currpage)-1] != '/') $currpage = $currpage . '/';
            $this->smarty->assign('currpage', $currpage);
            $this->smarty->assign('currpage_escaped', urlencode($currpage));

            //Get current URL without language
            $currlocat = preg_replace('/\/'.LANGUAGE.'/', '', $currpage);
            $this->smarty->assign('currlocat', $currlocat);

            //Initialize smarty variables
            $this->smarty->assign('language', LANGUAGE);
            $this->smarty->assign('language_ca', 'ca');
            $this->smarty->assign('language_es', 'es');
            $this->smarty->assign('language_en', 'en');
            $this->smarty->assign('title', 'Marc\s Programming Blog');
            $this->smarty->assign('accesslevel', (isSet($_SESSION['user'])) ? $_SESSION['user']->getAccess() : ACCESS_NONE);

            //Set user login status
            $this->smarty->assign('logedin', (isSet($_SESSION['user'])) ? TRUE : FALSE);
            
            //Initialize templates list & directories
            //$this->smarty->addTemplateDir(PATH_FWK_TEMPLATE);
            $this->templates = array();

            //Initialize headers
            $this->includes_css = array();
            $this->includes_js = array();
            $this->smarty->assign('includes_css', $this->includes_css);
            $this->smarty->assign('includes_js', $this->includes_js);
        }

        //* ********************************************* *//
        //* ** PUBLIC METHODS                          ** *//
        //* ********************************************* *//

        public static function getInstance(){
            if(self::$instance == null) self::$instance = new SmartyView();
            return self::$instance;
        }

        public function addDirectory($directory){
            $this->smarty->addTemplateDir($directory);
        }

        public function addTemplate($template){
            $this->templates[count($this->templates)] = $template;
        }

        public function draw(){
            // Draw content
            for($i=0;$i<count($this->templates);$i++){
                $this->smarty->display($this->templates[$i]);
            }
        }

        //* ********************************************* *//
        //* ** VARIABLES INITIALIZATION                ** *//
        //* ********************************************* *//

        public function includeCSS($name){
            $this->includes_css[count($this->includes_css)] = $name;
            $this->smarty->assign('includes_css', $this->includes_css);
        }

        public function includeJS($name){
            $this->includes_js[count($this->includes_js)] = $name;
            $this->smarty->assign('includes_js', $this->includes_js);
        }

        public function setTitle($title){
            $this->smarty->assign('title', $title);
        }

        public function assign($variable, $value){
            $this->smarty->assign($variable, $value);
        }
    }
?>