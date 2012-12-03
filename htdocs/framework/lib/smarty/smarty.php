<?php
    include_once('Smarty.class.php');

    class SmartySL extends Smarty{
	    private $path = 'C:/Temp/';
		
		public function __construct(){
		    parent::__construct();
			$this->template_dir= PATH_FWK_TEMPLATE;
			$this->compile_dir = $this->path.'smarty_compile';
			$this->config_dir = $this->path.'smarty_config';
			$this->cache_dir = $this->path.'smarty_cache';
		}
	}
?>