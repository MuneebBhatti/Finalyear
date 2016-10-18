<?php
include_once './DbConnect.php';
function changepwd() {
         $response = array();
		 
		
	 $db = new DbConnect();
	
       $uname = $_POST["uname"];
       $password = $_POST["pword"];

        
	  $sql="UPDATE teacher  
	  SET password='$password'
	  WHERE id='$uname' ";
	  $result = mysql_query($sql) or die(mysql_error());
        if ($result) {
		
					$response["error"] = false;
					$response["message"] = "Succssed ";
					
				  echo json_encode($response);
				  
				  return;		
		}
         else{
			
			$response["error"] = true;
            $response["message"] = "Failed ";
			echo json_encode($response);
           
        }

		 
  } 
       
   
changepwd();
?>