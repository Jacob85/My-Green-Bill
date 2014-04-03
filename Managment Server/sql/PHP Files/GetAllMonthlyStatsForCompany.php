<?php
 
    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    
    $insert_query="CALL GetAllMonthlyStatsForCompany('$_GET[id]')";
    
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
                "<td>" . $row['num_of_massages_sent'] . "</td><td>" . $row['num_of_sucsses_massages_sent'] ."</td>".
                "<td>" . $row['num_of_pending_massages'] . "</td><td>" . $row['num_of_failed_massages'] ."</td>";
        echo "</tr>";
    }
    
    echo "</table>";
?>

<br>
<button onclick="location.href='index.php';">Back To Query Page</button>
