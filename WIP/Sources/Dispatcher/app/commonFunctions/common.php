<?php

	// Function for basic field validation (present and neither empty nor only white space)	
	function IsNullOrEmptyString($string){
		return (!isset($string) || trim($string)==='');
	} 
	
?>