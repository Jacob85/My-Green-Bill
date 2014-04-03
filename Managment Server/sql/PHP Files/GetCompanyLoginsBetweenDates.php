<?php

    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    
    $insert_query="CALL GetCompanyLoginsBetweenDates('$_POST[id]','$_POST[startDate]','$_POST[endDate]')";
    
    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "<table border='1' cellpadding='10'>";
    
    echo "<tr>";
    echo "<td>Company Name</td><td>Company ID</td><td>Log in Date</td><td>Log in Time</td>";
    echo "</tr>";
    
    while ($row = mysql_fetch_assoc($result))
    {
        echo "<tr>";
        echo "<td>" . $row['name'] ."</td><td>" . $row['id'] . "</td><td>" . $row['date'] . "</td>".
                "<td>" . $row['time'] . "</td>";
        echo "</tr>";
    }
    
    echo "</table>";
?>

<br>
<button onclick="location.href='index.php';">Back To Query Page</button>
