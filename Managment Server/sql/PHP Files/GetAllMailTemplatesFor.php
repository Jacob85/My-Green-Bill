<?php

    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    

    $insert_query="CALL GetAllMailTemplatesFor('$_POST[target]')";

    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "<table border='1' cellpadding='10'>";
    
    echo "<tr>";
    echo "<td>ID</td><td>Name</td><td>Description</td><td>Context</td><td>Path to File</td><td>Date Created</td>";
    echo "</tr>";
    
    while ($row = mysql_fetch_assoc($result))
    {
        echo "<tr>";
        echo "<td>" . $row['id'] ."</td><td>" . $row['name'] . "</td><td>" . $row['description'] . "</td>".
                "<td>" . $row['context'] . "</td><td>" . $row['path_to_file'] . "</td><td>" . $row['create_date'] . "</td>";
        echo "</tr>";
    }
    
    echo "</table>";
?>

<br>
<button onclick="location.href='index.php';">Back To Query Page</button>
