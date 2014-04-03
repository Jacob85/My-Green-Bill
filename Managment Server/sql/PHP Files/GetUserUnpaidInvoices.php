<?php
 
    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    
    $insert_query="CALL GetUserUnpaidInvoices('$_GET[id]')";
    
    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "<table border='1' cellpadding='10'>";
    
    echo "<tr>";
    echo "<td>First Name</td><td>Last Name</td><td>Rank</td><td>Issue Date</td><td>Due Date</td><td>Content</td><td>Is Paid</td>";
    echo "</tr>";
    
    while ($row = mysql_fetch_assoc($result))
    {
        echo "<tr>";
        echo "<td>" . $row['first_name'] ."</td><td>" . $row['last_name'] ."</td><td>" . $row['rank'] . "</td>".
                "<td>" . $row['issue_date'] . "</td><td>" . $row['due_date'] ."</td>".
                "<td>" . $row['content'] . "</td><td>" . $row['is_paid'] ."</td>";
        echo "</tr>";
    }
    
    echo "</table>";
?>

<br>
<button onclick="location.href='index.php';">Back To Query Page</button>
