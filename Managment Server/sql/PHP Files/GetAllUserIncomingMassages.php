<?php

    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    
    $insert_query="CALL GetAllUserIncomingMassages('$_POST[id]')";
    
    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "<table border='1' cellpadding='10'>";
    
    echo "<tr>";
    echo "<td>First Name</td><td>Last Name</td><td>Send To</td><td>Send From</td><td>Subject</td>
            <td>Content</td><td>File Attached Name</td><td>File path</td>";
    echo "</tr>";
    
    while ($row = mysql_fetch_assoc($result))
    {
        echo "<tr>";
        echo "<td>" . $row['first_name'] ."</td><td>" . $row['last_name'] . "</td><td>" . $row['send_to'] . "</td>".
                "<td>" . $row['send_from'] . "</td><td>" . $row['subject'] . "</td><td>" . $row['content'] . "</td>".
                 "<td>" . $row['name'] . "</td><td>" . $row['path'] . "</td>";
        echo "</tr>";
    }
    
    echo "</table>";
?>

<br>
<button onclick="location.href='index.php';">Back To Query Page</button>
