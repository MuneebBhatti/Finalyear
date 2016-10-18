<?php
include_once './DbConnect.php';
function checkLogin() {
         $response = array();
		 
		
	 $db = new DbConnect();
	
       $uname = $_POST["uname"];
       $password = $_POST["pword"];

        
	  $sql="SELECT * 
	  FROM teacher 
	  WHERE id='$uname' && password='$password' ";
	  $result = mysql_query($sql) or die(mysql_error());
        if (mysql_num_rows($result)!= 0) {
			
				 while($row = mysql_fetch_array($result))
					  {
						  $tch_id = $row["id"];
						  $tch_name = $row["Name"];
						  $tch_pwd = $row["password"];
					  }
					$response["error"] = false;
					$response["message"] = "Succssed ";
					$response["teacher"]= $tch_id ;
					$response["teacher_name"]=$tch_name;
					$response["teacher_password"]=$tch_pwd;
					
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