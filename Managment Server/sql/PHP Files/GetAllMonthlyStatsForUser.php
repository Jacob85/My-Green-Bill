<?php
 
    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    
    $insert_query="CALL GetAllMonthlyStatsForUser('$_GET[id]')";
    
    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "<table border='1' cellpadding='10'>";
    
    echo "<tr>";
    echo "<td>Month</td><td>Log Ins</td><td>Total Massages</td><td>Sent Massages</td><td>Pending Massages</td><td>Failed Massages</td>";
    echo "</tr>";
    
    while ($row = mysql_fetch_assoc($result))
    {
        echo "<tr>";
        echo "<td>" . $row['month_name'] ."</td><td>" . $row['num_of_log_in'] . "</td>".
                "<td>" . $row['massage_recived'] . "</td><td>" . $row['massage_success'] ."</td>".
                "<td>" . $row['massage_panding'] . "</td><td>" . $row['massage_failed'] ."</td>";
        echo "</tr>";
    }
    
    echo "</table>";
?>

<br>
<button onclick="location.href='index.php';">Back To Query Page</button>
