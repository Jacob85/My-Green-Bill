<?php
 
    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    
    $insert_query="CALL GetAllCompanyContacts('$_GET[id]')";
    
    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "<table border='1' cellpadding='10'>";
    
    echo "<tr>";
    echo "<td>Contact Name</td><td>Position</td><td>email</td><td>Phone</td><td>fax</td><td>office</td>";
    echo "</tr>";
    
    while ($row = mysql_fetch_assoc($result))
    {
        echo "<tr>";
        echo "<td>" . $row['name'] ."</td><td>" . $row['position'] . "</td>".
                "<td>" . $row['email'] . "</td><td>" . $row['phone_number'] ."</td>".
                "<td>" . $row['fax_number'] . "</td><td>" . $row['office_number'] ."</td>";
        echo "</tr>";
    }
    
    echo "</table>";
?>

<br>
<button onclick="location.href='index.php';">Back To Query Page</button>
