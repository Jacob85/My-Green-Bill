<?php

    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    
    $insert_query="CALL AddSubscriptionPlan('$_POST[name]', '$_POST[description]', '$_POST[pathToFile]', '$_POST[target_audience]');";
    
    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "The plan " . $_POST["name"] . " was added.";
?>

<br>
<button onclick="location.href='index.php';">Back To Query Page</button>

