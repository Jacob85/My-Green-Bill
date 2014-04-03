<?php

    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    
    $insert_query="CALL GetAllUserInvoices('$_POST[id]')";
    
    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "<table border='1' cellpadding='10'>";
    
    echo "<tr>";
    echo "<td>ID</td><td>Billin Info ID</td><td>Content</td><td>Issue Date</td><td>Due Date</td><td>Amount</td>";
    echo "</tr>";
    
    while ($row = mysql_fetch_assoc($result))
    {
        echo "<tr>";
        echo "<td>" . $row['id'] ."</td><td>" . $row['billing_info_id'] . "</td><td>" . $row['content'] . "</td>".
                "<td>" . $row['issue_date'] . "</td><td>" . $row['due_date'] . "</td><td>" . $row['amount'] . "</td>";
        echo "</tr>";
    }
    
    echo "</table>";
?>

<br>
<button onclick="location.href='index.php';">Back To Query Page</button>
