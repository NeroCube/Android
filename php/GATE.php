<?php
// array for JSON response
$response = array();
include("GATE_config.php");
// check for required fields
if (isset($_POST['db'])&&isset($_POST['sql'])&&isset($_POST['mode']) ) {
	$db = $_POST['db'];
	$sql = $_POST['sql'];
	$mode = $_POST['mode'];
	//print_r($mode);
    // connecting to db
    $con = mysql_connect($localhost,$uid,$pw);
    if (!$con)
	{
	$response["success"] = 0;
    $response["message"] = "Database Connect Error";
	}
	else
	{
	//Select the Database
    mysql_select_db($db,$con);
	mysql_query("SET NAMES utf8;",$con);
	$result = mysql_query($sql,$con);
	//$result = mysql_query($sql,$con)or die(mysql_error());
	//I1D2U3S4
	if ($mode==1){
	if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Comand successfully inserted";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = mysql_error();
        // echoing JSON response
        echo json_encode($response);
    }
	}
	else if($mode==2){
	if (mysql_affected_rows() > 0) {
        $response["success"] = 2;
        $response["message"] = "objects successfully deleted";
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "No objects found";
        echo json_encode($response);
    }
	}
	else if ($mode==3){
	if ($result) {
        // successfully inserted into database
        $response["success"] = 3;
        $response["message"] = "Comand successfully updated";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = mysql_error();
        // echoing JSON response
        echo json_encode($response);
    }
	}
	else if($mode==4){
	if (mysql_num_rows($result) > 0) 
	{
    $response["objects"] = array();
    while ($row = mysql_fetch_array($result)) {
        $object = array();
		for ($i = 0; $i < mysql_num_fields($result); $i++) 
		{
		  $object[mysql_field_name($result, $i)] = $row[$i];
		} 
        array_push($response["objects"], $object);
    }
    $response["success"] = 4;
    echo json_encode($response);
	} 
	else {
    // no objects found
    $response["success"] = 0;
    $response["message"] = "SQL Comand Error,No objects found";

    // echo no users JSON
    echo json_encode($response);
	}
	}
	else
	{
	$response["success"] = 0;
    $response["message"] = "Unknown SQL Comand Mode";
	}
  } 
}
else{
  $response["success"] = 0;
  $response["message"] = "Upload parameters is incomplete";
} 
mysql_close($con);
?>

