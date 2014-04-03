<?php

    $host = "localhost"; 
    $user = "root"; 
    $pass = ""; 
    $db = "mydb";
    
    $connection = mysql_connect($host, $user, $pass);
    
    mysql_select_db($db);
    
    $insert_query="CALL AddContactToCompany('$_POST[id]', '$_POST[first_name]', '$_POST[position]', '$_POST[email]',
                                            '$_POST[phone_number]', '$_POST[office]', '$_POST[fax]');";

    $result = mysql_query($insert_query);
    
    if (!$result)
    {
        die('Could not query: ' . mysql_error());
    }
    
    mysql_close($connection);
    
    echo "The contact men " . $_POST["first_name"] . " was added to the company contact persons.";
?>
<br>

<a href="http://localhost/dbcourse/GetAllCompanyContacts.php?id=<?php echo $_POST["id"] ?>">see all of the company contacts</a>
<button onclick="location.href='index.php';">Back To Query Page</button>
