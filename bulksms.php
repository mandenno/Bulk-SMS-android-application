<?php
define('HOST','localhost');
define('USER','1295595');
define('PASS','1062142014');
define('DB','1295595');
 
$con = mysqli_connect(HOST,USER,PASS,DB);
 
$sql = "select * from University_Students_Contacts order by names asc";
 
$res = mysqli_query($con,$sql);
 
$result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,
array('phone'=>'+254'.$row['phone'].'',
'names'=>$row['names'],
'email'=>$row['email']
));
}
 
echo json_encode(array("contacts"=>$result));
 
mysqli_close($con);
 
?>
