<?php

    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    $rank = "";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    
    if ($_POST["sub_plan"] == 2 || $_POST["sub_plan"] == 4)
    {
        $rank = "platinum";
    }
    else
    {
        $rank = "gold";
    }
    
    $insert_query="CALL AddCompany('$_POST[id]', '$_POST[mail]', '$_POST[company_name]',
                                '$_POST[pass]', '$_POST[sub_plan]', '$rank',
                                '$_POST[street]', '$_POST[house_number]', '$_POST[city]', '$_POST[postal_code]',
                                '$_POST[country]');";
    
    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "The company " . $_POST["company_name"] . " was added.";
?>

<br>
<button onclick="location.href='index.php';">Back To Query Page</button>