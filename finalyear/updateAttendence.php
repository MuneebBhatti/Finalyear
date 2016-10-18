<?php
include_once './DbConnect.php';
function update_attendence() {
         $response = array();
		 
		
	 $db = new DbConnect();
	 
	  $sec_id = $_GET["sec_id"];
	  $std_id = $_GET["std_id"];
      $attd_id= $_GET["attd_id"];
      $date   = $_GET["dt"];
	  $student_id=array();
	  $student_name=array();

        
	  $sql="INSERT INTO attendence (sectionid,studentid,status,date) 
	        VALUES ('$sec_id','$std_id','$attd_id','$date')";
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
       
   
update_attendence();
?>