<?php

    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
     mysql_select_db($db);
    
     
    $fileName = $_POST["fileName"];
    $filePath = $_POST["filePath"];
    $cc = $_POST["cc"];
    
    $fileName = !empty($fileName) ? "'$fileName'" : "NULL";
    $filePath = !empty($filePath) ? "'$filePath'" : "NULL";
    $cc = !empty($cc) ? "'$cc'" : "NULL";

    
    $insert_query="CALL NewMassage('$_POST[fromEmail]', '$_POST[fromID]', '$_POST[toEmail]', '$_POST[toID]',
                                             $cc, '$_POST[subject]', '$_POST[content]',
                                            '$_POST[status]', $fileName,$filePath);";
    
   
    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "massage with the subject  " . $_POST["subject"] . " was added to the dataBase.";
?>
<br>

<button onclick="location.href='index.php';">Back To Query Page</button>
