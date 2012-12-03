<?php
    //Root path
	define('PATH', dirname($_SERVER['DOCUMENT_ROOT']).'/');
	
	// Framework application directories
	define('PATH_FWK', PATH.'framework/');
	define('PATH_FWK_CLASS', PATH_FWK.'class/');
	define('PATH_FWK_TEMPLATE', PATH_FWK.'template/');
	define('PATH_FWK_LIB', PATH_FWK.'lib/');
	define('PATH_FWK_LANG', PATH_FWK.'lang/');
	
	// Temporany directory
	define('PATH_TEMP', PATH.'temp/');
	
	// User access levels
	define('ACCESS_ADMIN', 10);
	define('ACCESS_COADMIN', 9);
	define('ACCESS_MANAGER', 8);
	define('ACCESS_SERVICES', 3);
	define('ACCESS_MINIMAL', 1);
	define('ACCESS_NONE', 0);
?>