<?php
include_once './DbConnect.php';
function checkLogin() {
         $response = array();
		 
		
	 $db = new DbConnect();
	   
	   $uname = $_POST["uname"];
       $email = $_POST["email"];
      
        
	  $sql="SELECT password 
	  FROM teacher 
	  WHERE id='$uname' && email='$email' ";
	  $result = mysql_query($sql) or die(mysql_error());
        if (mysql_num_rows($result)!= 0) {
			
				 while($row = mysql_fetch_array($result))
					  {
						  $password = $row["password"];
						  
					  }
					$response["error"] = false;
					$response["message"] = "Succssed ";
					$response["password"]= $password ;
					
				  echo json_encode($response);
				  
				  return;
			
			
		}
         else{
			
			$response["error"] = true;
            $response["message"] = "Failed ";
			echo json_encode($response);
           
        }

		 
  } 
       
   
checkLogin();
?>