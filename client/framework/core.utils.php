<?php
	class UtilsCore{
		static public function extractUrlParameters(){
			$pars = explode('?', $_SERVER['REQUEST_URI']);
			$pars = explode('/', $pars[0]);

			$i = 0;
			while(isSet($pars[$i])){
				if($pars[$i] == ''){
					for($j=$i;$j<count($pars)-1;$j++){
						$pars[$j] = $pars[$j+1];
					}
					unset($pars[count($pars)-1]);
				}else{
					$i++;
				}
			}
			
			return $pars;
		}
	
	    //* ********************************************* *//
        //* ** DATE & TIME FORMATTING                  ** *//
        //* ********************************************* *//
		
		static public function getDateFormat($timestamp){
			$ret = date('d ', $timestamp);
			$ret = $ret . self::getMonthName(date('m', $timestamp));
			$ret = $ret . date(' Y', $timestamp);
			return $ret;
		}
		
		static public function getMonthName($month){
			include_once(PATH_FWK_LANG.'datetime_'.LANGUAGE.'.php');
			switch((int)$month){
				case 1:  return MONTH_JAN;
				case 2:  return MONTH_FEB;
				case 3:  return MONTH_MAR;
				case 4:  return MONTH_APR;
				case 5:  return MONTH_MAY;
				case 6:  return MONTH_JUN;
				case 7:  return MONTH_JUL;
				case 8:  return MONTH_AUG;
				case 9:  return MONTH_SEP;
				case 10: return MONTH_OCT;
				case 11: return MONTH_NOV;
				case 12: return MONTH_DEC;
			}
		}
	
	    //* ********************************************* *//
        //* ** PARAMETERS FORMAT CHECKING              ** *//
        //* ********************************************* *//
	
		static public function checkID($id){
			$check = preg_match('/^([0-9]+)$/', $id);
			if($check == 0) return false;
			return true;
		}
		
		static public function checkName($name){
			$check = preg_match('/^([A-Za-z0-9\.\_]){2,60}$/', $name);
			if($chack == 0) return false;
			return true;
		}
		
		static public function checkUsername($username){
			$check = preg_match('/^([A-Za-z0-9\.\_]){8,15}$/', $username);
			if($check == 0) return false;
			return true;
		}
		
		static public function checkPassword($password){
			$check = preg_match("/^([A-Za-z0-9\.\_]){8,15}$/", $password);
			if($check == 0) return false;
			return true;
		}
		
		static public function checkEmail($email){
			$check = preg_match("/^([A-Za-z0-9\.\_]){2,30}\@([A-Za-z0-9\_]){2,30}\.([A-Za-z]){2,3}$/", $email);
			if($check == 0) return false;
			return true;
		}
		
		static public function checkAccess($access){
			$check = preg_match('/^([0-9]+)$/', $access);
			if($check == 0) return false;
			return true;
		}
	}
?>