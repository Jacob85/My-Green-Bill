<?php

    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    
    $insert_query="CALL CreateNewInvoice('$_POST[for]', '$_POST[id]','$_POST[email]','$_POST[amount]'
                            ,'$_POST[issue_date]','$_POST[due_date]','$_POST[content]','$_POST[is_paid]')";
    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "The new invoice was added to " . $_POST["id"] . " - " . $_POST["email"] . ".";
?>

<br>
<button onclick="location.href='index.php';">Back To Query Page</button>