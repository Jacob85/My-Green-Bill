<?php

    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    
    $insert_query="CALL AddMonthlyStatForCompany('$_POST[id]', '$_POST[startDate]', '$_POST[endDate]');";                                    

    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "The monthly stats for company id  " . $_POST["id"] . " was added to the company monthly stats table.";
?>
<br>

<a href="http://localhost/dbcourse/GetAllMonthlyStatsForCompany.php?id=<?php echo $_POST["id"] ?>">see the monthly stats table for this company</a>
<button onclick="location.href='index.php';">Back To Query Page</button>
