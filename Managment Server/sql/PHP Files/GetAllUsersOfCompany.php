<?php

    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    
    $insert_query="CALL GetAllUsersOfCompany('$_GET[id]', '$_GET[email]')";
    
    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "<table border='1' cellpadding='10'>";
    
    echo "<tr>";
    echo "<td>Company ID</td><td>Company Name</td><td>Company Email</td><td>User ID</td><td>User Email</td><td>User First Name</td><td>User Last Name</td>";
    echo "</tr>";
    
    while ($row = mysql_fetch_assoc($result))
    {
        echo "<td>" . $row['company_id'] ."</td><td>" . $row['name'] . "</td><td>" . $row['company_email'] . "</td>".
                "<td>" . $row['user_id'] . "</td><td>" . $row['user_email'] ."</td><td>" . $row['first_name'] ."</td>
                    <td>" . $row['last_name'] ."</td>";
        echo "</tr>";
    }
    
    echo "</table>";
?>

<br>
<button onclick="location.href='index.php';">Back To Query Page</button>
