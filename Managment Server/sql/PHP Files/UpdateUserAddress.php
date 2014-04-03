<?php

    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    
    $insert_query="CALL UpdateUserAddress('$_POST[id]', '$_POST[street]', '$_POST[house_number]', '$_POST[city]',
                                '$_POST[postal_code]', '$_POST[country]');";
    
    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "The user " . $_POST["id"] . " was updated.";
?>

<br>
<button onclick="location.href='index.php';">Back To Query Page</button>