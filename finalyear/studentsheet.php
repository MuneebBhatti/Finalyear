<?php
include_once './DbConnect.php';
function select_students() {
         $response = array();
		 
		
	 $db = new DbConnect();
	
      $sec_id = $_GET["sec_id"];
      
	  $student_id=array();
	  $student_name=array();

        
	  $sql="SELECT distinct student.id,student.Name 
	        FROM student 
	        INNER JOIN enrollment 
			ON student.id=studentid 
			WHERE sectionid= '$sec_id'
            order by student.id ASC
			";
	 $i=0;
	 $result = mysql_query($sql) or die(mysql_error());
        if (mysql_num_rows($result)!= 0) {
			
				 while($row = mysql_fetch_array($result))
					  {
						  
						  $student_id[$i] = $row["id"];
						  $student_name[$i] = $row["Name"];
						  $i++;
					  }
					$response["error"] = false;
					$response["message"] = "Succssed ";
					$response["student_id"]=$student_id;
					$response["student_name"]=$student_name;
				  echo json_encode($response);
				  
				  return;
			
			
		}
         else{
			
			$response["error"] = true;
            $response["message"] = "Failed ";
			echo json_encode($response);
           
        }

		 
  } 
       
   
select_students();
?>