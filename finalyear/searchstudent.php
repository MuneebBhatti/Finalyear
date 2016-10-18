<?php
include_once './DbConnect.php';
function select_sections() {
         $response = array();
		 $status=array();
	     $date=array();
		
	 $db = new DbConnect();
	
      $sec_id = $_GET["sec_id"];
	  $student_id = $_GET["std_id"];
      
        
	  $sql="SELECT status,date,Name
	        FROM attendence
			INNER JOIN student
			ON attendence.studentid=student.id
	        WHERE  sectionid='$sec_id' AND studentid= '$student_id'      
			";
	 
	 $i=0;
	 $result = mysql_query($sql) or die(mysql_error());
        if (mysql_num_rows($result)!= 0) {
			
				 while($row = mysql_fetch_array($result))
					  {
						  $name=$row["Name"];
						  $status[$i] = $row["status"];
						  $date[$i] = $row["date"];
						  $i++;
					  }
					$response["error"] = false;
					$response["message"] = "Succssed ";
					$response["name"]=$name;
					$response["status"]=$status;
					$response["date"]=$date;
				  echo json_encode($response);
				  
				  return;	
			
		}
         else{
			
			$response["error"] = true;
            $response["message"] = "Failed ";
			echo json_encode($response);
           
        }

		 
  } 
       
   
select_sections();
?>