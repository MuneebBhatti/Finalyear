<?php
class DbConnect {  

	private $host = "localhost";
	private $user = "root";
	private $password = "";
	private $database = "ams";
	
        
	
	
	function __construct() {
		$conn = $this->connectDB();
		if(!empty($conn)) {
			$this->selectDB($conn);
		}
	}
	
	function connectDB() {
	   
		$conn = mysql_connect($this->host,$this->user,$this->password);
		return $conn;
	}
	
	function selectDB($conn) {
	
		mysql_select_db($this->database,$conn);
	}
	
	function close(){
		mysql_close ();
	}

}
?>


