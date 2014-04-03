<?php

    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    
    $insert_query="CALL GetCompanyAddress('$_GET[id]', '$_GET[email]')";
    
    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "<table border='1' cellpadding='10'>";
    
    echo "<tr>";
    echo "<td>Company ID</td><td>Company Name</td><td>Email</td><td>Country</td><td>City</td><td>Street Name</td><td>House Number</td><td>Postal Code</td>";
    echo "</tr>";
    
    while ($row = mysql_fetch_assoc($result))
    {
        echo "<td>" . $row['id'] ."</td><td>" . $row['name'] . "</td><td>" . $row['email'] . "</td>".
                "<td>" . $row['country'] . "</td><td>" . $row['city'] ."</td><td>" . $row['street_name'] ."</td>
                    <td>" . $row['house_number'] ."</td><td>" . $row['postal_code'] ."</td>";
        echo "</tr>";
    }
    
    echo "</table>";
?>

<br>
<button onclick="location.href='index.php';">Back To Query Page</button>
