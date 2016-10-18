<?php
include_once './DbConnect.php';
function select_sections() {
         $response = array();
		 $section_id=array();
	     $section_name=array();
		
	 $db = new DbConnect();
	
      $teacher_id = $_GET["tech_id"];
	  $course_name = $_GET["course_name"];
      
        
	  $sql="SELECT section.id,section.Name 
	        FROM section 
	        INNER JOIN course 
			ON course.id=section.courseid 
			WHERE section.teacherid= '$teacher_id' AND course.Name='$course_name' 
			ORDER by Name ASC
			";
	 
	 $i=0;
	 $result = mysql_query($sql) or die(mysql_error());
        if (mysql_num_rows($result)!= 0) {
			
				 while($row = mysql_fetch_array($result))
					  {
						  
						  $section_id[$i] = $row["id"];
						  $section_name[$i] = $row["Name"];
						  $i++;
					  }
					$response["error"] = false;
					$response["message"] = "Succssed ";
					$response["section_id"]=$section_id;
					$response["section_name"]=$section_name;
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