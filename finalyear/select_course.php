<?php
include_once './DbConnect.php';
function select_courses() {
         $response = array();
		 $cr_name=array();
	   
		 $db = new DbConnect();
	
       $teacher_id = $_GET["tech_id"];
      
	  
	  $sql="SELECT distinct course.Name 
	  FROM course 
	  INNER JOIN section 
	  ON course.id=section.courseid 
	  where section.teacherid='$teacher_id'  ";
	  
	  $i=0;
	  $result = mysql_query($sql) or die(mysql_error());
        if (mysql_num_rows($result)!= 0) {
			
				 while($row = mysql_fetch_array($result))
					  {
						
						  $cr_name[$i] = $row["Name"];
						  $i++;
					  }
					$response["error"] = false;
					$response["message"] = "Succssed ";
					$response["course_name"]=$cr_name;
					
				  echo json_encode($response);
				  
				  return;
			
			
		}
         else{
			
			$response["error"] = true;
            $response["message"] = "Failed ";
			echo json_encode($response);
           
        }
	 
  } 
       
select_courses();
?>