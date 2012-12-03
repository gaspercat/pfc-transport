<?php
    class ApplicationController{
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
            
            // Include stylesheet
            $this->smarty->includeCSS('/jsapp/style.css');

            // Include JS application : basics
            $this->smarty->includeJS('/jsapp/application.js');
            $this->smarty->includeJS('/jsapp/loader.js');
            $this->smarty->includeJS('/jsapp/repository.js');
            $this->smarty->includeJS('/jsapp/global_events.js');
            
            // Include JS application : data types
            $this->smarty->includeJS('/jsapp/data/date.js');
            $this->smarty->includeJS('/jsapp/data/time.js');
            $this->smarty->includeJS('/jsapp/data/interval.js');
            
            // Include JS application : GUI elements
            $this->smarty->includeJS('/jsapp/gui/tabs.js');
            $this->smarty->includeJS('/jsapp/gui/menu.js');
            $this->smarty->includeJS('/jsapp/gui/list.js');
            $this->smarty->includeJS('/jsapp/gui/calendar.js');
            $this->smarty->includeJS('/jsapp/gui/schedule.js');
            $this->smarty->includeJS('/jsapp/gui/wrapper/vignette.js');
            $this->smarty->includeJS('/jsapp/gui/element/product.js');
            $this->smarty->includeJS('/jsapp/gui/element/driver.js');
            $this->smarty->includeJS('/jsapp/gui/field/numeric.js');
            $this->smarty->includeJS('/jsapp/gui/field/text.js');
            $this->smarty->includeJS('/jsapp/gui/field/time.js');
            
            // Include JS application : Windows
            $this->smarty->includeJS('/jsapp/wdw/wdwMenu.js');
            $this->smarty->includeJS('/jsapp/wdw/wdwProducts.js');
            $this->smarty->includeJS('/jsapp/wdw/wdwDrivers.js');

            // Draw view
            $this->smarty->addTemplate('application.tpl');
        }
    }
?>