<!--
To change this template, choose Tools | Templates
and open the template in the editor.
-->
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
        <!--Adding the CSS style file-->
        <link rel="stylesheet" href="style.css">
    </head>
    <body>
        <table border="1">
            <tr>
                <td>
                    <div class="numberClass">
                        1.
                    </div>
                    <!-- Add new User Form -->
                    <div align="center">
                        <h3>Add New User:</h3>
                        <form action="addUser.php" method="post">
                            <table>
                                <tr><td> ID: </td><td> <input type="text" name="id"> </td></tr>
                                <tr><td> E-Mail: </td><td> <input type="text" name="mail"> </td></tr>
                                <tr><td> First name: </td><td> <input type="text" name="first_name"> </td></tr>
                                <tr><td> Last name: </td><td> <input type="text" name="last_name"> </td></tr>
                                <tr><td> Password: </td><td> <input type="text" name="pass"> </td></tr>
                                <tr><td> Subscription Plan: </td><td> <input type="number" name="sub_plan"> </td></tr>
                            </table>
                            <h3>Address</h3>
                            <table>
                                <tr><td> Street: </td><td> <input type="text" name="street"> </td></tr>
                                <tr><td> House Number: </td><td> <input type="text" name="house_number"> </td></tr>
                                <tr><td> City: </td><td> <input type="text" name="city"> </td></tr>
                                <tr><td> Postal Code: </td><td> <input type="text" name="postal_code"> </td></tr>
                                <tr><td> Country: </td><td> <input type="text" name="country"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        2.
                    </div>
                    <!-- >Add new company form  --> 
                    <div align="center">
                        <h3>Add New Company:</h3>
                        <form action="addNewCompany.php" method="post">
                            <table>
                                <tr><td> ID: </td><td> <input type="text" name="id"> </td></tr>
                                <tr><td> E-Mail: </td><td> <input type="text" name="mail"> </td></tr>
                                <tr><td> Company Name: </td><td> <input type="text" name="company_name"> </td></tr>
                                <tr><td> Password: </td><td> <input type="text" name="pass"> </td></tr>
                                <tr><td> Subscription Plan: </td><td> <input type="number" name="sub_plan"> </td></tr>
                            </table>
                            <h3>Address</h3>
                            <table>
                                <tr><td> Street: </td><td> <input type="text" name="street"> </td></tr>
                                <tr><td> House Number: </td><td> <input type="text" name="house_number"> </td></tr>
                                <tr><td> City: </td><td> <input type="text" name="city"> </td></tr>
                                <tr><td> Postal Code: </td><td> <input type="text" name="postal_code"> </td></tr>
                                <tr><td> Country: </td><td> <input type="text" name="country"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        3.
                    </div>
                    <!-- >Add Subscription plan Form -->
                    <div align="center">
                        <h3>Add Subscription Plan:</h3>
                        <form action="addSubscriptionPlan.php" method="post">
                            <table>
                                <tr><td> Name: </td><td> <input type="text" name="name"> </td></tr>
                                <tr><td> Description: </td><td> <input type="text" name="description"> </td></tr>
                                <tr><td> Path to File: </td><td> <input type="text" name="pathToFile"> </td></tr>
                                <tr><td> Target Audience: </td><td> <select name="target_audience">
                                                                    <option value="user">Users</option>
                                                                    <option value="company">Company</option>                                           
                                                                    </select> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        4.
                    </div>
                    <!-- >Get Subscription plan Form -->
                    <div align="center">
                        <h3>Get Subscription Plan For:</h3>
                        <form action="GetSubsciptionPlanFor.php" method="post">
                            <table>
                                <tr><td>Get Subscription Plan For: </td><td> <select name="target_audience">
                                                                    <option value="user">Users</option>
                                                                    <option value="company">Company</option>
                                                                    <option value="All">All</option>  
                                                                    </select> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="numberClass">
                        5.
                    </div>
                    <!-- >Get All Login Events in requested Date  --> 
                    <div align="center">
                        <h3>Get All Login Events In Requested Date:</h3>
                        <form action="GetAllLogInEventAtDay.php" method="post">
                            <table>
                                <tr><td>Get All Login Events in requested Date: </td><td><input type="date" name="date"></td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        6.
                    </div>
                    <!-- Get User Invoices Form -->
                    <div align="center">
                        <h3>Get User Invoices:</h3>
                        <form action="getUserInvoices.php" method="post">
                            <table>
                                <tr><td> ID: </td><td> <input type="text" name="id"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        7.
                    </div>
                    <!-- Get User Logins Form -->
                     <div align="center">
                        <h3>Get User Logins:</h3>
                        <form action="GetUserLogins.php" method="post">
                            <table>
                                <tr><td> ID: </td><td> <input type="text" name="id"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        8.
                    </div>
                    <!-- Get User Company Form -->
                    <div align="center">
                        <h3>Get Company Logins:</h3>
                        <form action="GetCompanyLogins.php" method="post">
                            <table>
                                <tr><td> ID: </td><td> <input type="text" name="id"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="numberClass">
                        9.
                    </div>
                    <!-- Get All User Messages From Specific Company -->
                    <div align="center">
                        <h3>Get All User Messages From Company:</h3>
                        <form action="GetAllUserIncomingMassagesFromCompany.php" method="post">
                            <table>
                                <tr><td>User ID: </td><td> <input type="text" name="user_id"> </td></tr>
                                <tr><td>Company ID: </td><td> <input type="text" name="company_id"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        10.
                    </div>
                    <!-- Add A Contact Man To Company Form -->
                    <div align="center">
                        <h3>Add Contact Man To Company:</h3>
                        <form action="AddContactToCompany.php" method="post">
                            <table>
                                <tr><td> Company ID: </td><td> <input type="text" name="id"> </td></tr>
                                <tr><td> First name: </td><td> <input type="text" name="first_name"> </td></tr>
                                <tr><td> Position in Company: </td><td> <input type="text" name="position"> </td></tr>
                                <tr><td> Phone number: </td><td> <input type="text" name="phone_number"> </td></tr>
                                <tr><td> E-Mail: </td><td> <input type="text" name="email"> </td></tr>                                
                                <tr><td> Fax: </td><td> <input type="text" name="fax"> </td></tr>
                                <tr><td> Office number: </td><td> <input type="text" name="office"> </td></tr>                                                             
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        11.
                    </div>
                    <!-- Get All The New Companies Who Joined The Service Between Certain Dates -->
                    <div align="center">
                        <h3>Get All Companies Who Joined The Service Between:</h3>
                        <form action="GetAllNewCompanysBetween.php" method="post">
                            <table>
                                <tr><td> Start Date: </td><td> <input type="date" name="startDate"> </td></tr> 
                                <tr><td> End Date: </td><td> <input type="date" name="endDate"> </td></tr>                                                           
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        12.
                    </div>
                    <!-- Get All The New Users Who Joined The Service Between Certain Dates -->
                    <div align="center">
                        <h3>Get All Users Who Joined The Service Between:</h3>
                        <form action="GetAllNewUsersBetween.php" method="post">
                            <table>
                                <tr><td> Start Date: </td><td> <input type="date" name="startDate"> </td></tr> 
                                <tr><td> End Date: </td><td> <input type="date" name="endDate"> </td></tr>                                                           
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="numberClass">
                        13.
                    </div>
                    <!-- Get All User Messages -->
                    <div align="center">
                        <h3>Get All User Messages:</h3>
                        <form action="GetAllUserIncomingMassages.php" method="post">
                            <table>
                                <tr><td> ID: </td><td> <input type="text" name="id"> </td></tr>                                                          
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        14.
                    </div>
                    <!-- Get All The Contact Men Of A Company Form -->
                    <div align="center">
                        <h3>Get All Company Contact Men:</h3>
                        <form action="GetAllCompanyContacts.php" method="get">
                            <table>
                                <tr><td> ID: </td><td> <input type="text" name="id"> </td></tr>                                                          
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        15.
                    </div>
                   <!-- Get All User Logins Between Certain Dates -->
                   <div align="center">
                        <h3>Get User Logins Between Dates:</h3>
                        <form action="GetUserLoginsBetweenDates.php" method="post">
                            <table>
                                <tr><td> ID: </td><td> <input type="text" name="id"> </td></tr>
                                <tr><td> Start Date: </td><td> <input type="date" name="startDate"> </td></tr> 
                                <tr><td> End Date: </td><td> <input type="date" name="endDate"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        16.
                    </div>
                    <!-- Get All Company Logins Between Certain Dates -->
                   <div align="center">
                        <h3>Get Company Logins Between Dates:</h3>
                        <form action="GetCompanyLoginsBetweenDates.php" method="post">
                            <table>
                                <tr><td> ID: </td><td> <input type="text" name="id"> </td></tr>
                                <tr><td> Start Date: </td><td> <input type="date" name="startDate"> </td></tr> 
                                <tr><td> End Date: </td><td> <input type="date" name="endDate"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="numberClass">
                        17.
                    </div>
                   <!-- Get All The Outgoing Messages Of A Company -->
                   <div align="center">
                        <h3>Get All Company Outgoing Messages:</h3>
                        <form action="GetAllCompanyOutgoingMassages.php" method="get">
                            <table>
                                <tr><td> ID: </td><td> <input type="text" name="id"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        18.
                    </div>
                    <!-- Get All The Outgoing Messages Of A Company To A Specific User-->
                    <div align="center">
                        <h3>Get All Company Outgoing Messages To Customer:</h3>
                        <form action="GetAllCompanyOutgoingMassagesToCostumer.php" method="get">
                            <table>
                                <tr><td>User ID: </td><td> <input type="text" name="user_id"> </td></tr>
                                <tr><td>Company ID: </td><td> <input type="text" name="company_id"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        19.
                    </div>
                    <!-- Get Company Address-->
                    <div align="center">
                        <h3>Get Company Address:</h3>
                        <form action="GetCompanyAddress.php" method="get">
                            <table>
                                <tr><td>Company ID: </td><td> <input type="text" name="id"> </td></tr>
                                <tr><td>Company Email: </td><td> <input type="text" name="email"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        20.
                    </div>
                    <!-- Get User Address-->
                    <div align="center">
                        <h3>Get User Address:</h3>
                        <form action="GetUserAddress.php" method="get">
                            <table>
                                <tr><td>User ID: </td><td> <input type="text" name="id"> </td></tr>
                                <tr><td>User Email: </td><td> <input type="text" name="email"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="numberClass">
                        21.
                    </div>
                    <!-- Get All The Company Customers-->
                    <div align="center">
                        <h3>Get All The Users Of A Certain Company:</h3>
                        <form action="GetAllUsersOfCompany.php" method="get">
                            <table>
                                <tr><td>Company ID: </td><td> <input type="text" name="id"> </td></tr>
                                <tr><td>Company Email: </td><td> <input type="text" name="email"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        22.
                    </div>
                    <!-- Get Mail Template Of The Different Kinds -->
                    <div align="center">
                        <h3>Get Mail Template For:</h3>
                        <form action="GetAllMailTemplatesFor.php" method="post">
                            <table>
                                <tr>
                                    <td>Get Mail Template For: </td>
                                    <td>
                                        <select name="target">
                                            <option value="welcome">Welcome</option>
                                            <option value="password_reset">Password Reset</option>
                                            <option value="monthly_update">Monthly Update</option>
                                            <option value="All">All</option>  
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        23.
                    </div>
                    <!-- Get The Mail Template Which Is Managed By -->
                    <div align="center">
                        <h3>Get Mail Template Managed By:</h3>
                        <form action="GetMailTemplateManagedBy.php" method="get">
                            <table>
                                <tr><td>Admin ID: </td><td> <input type="text" name="id"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        24.
                    </div>
                    <!-- Add Monthly Statistics To A Certain Company -->
                    <div align="center">
                        <h3>Add Monthly Statistics To Company:</h3>
                        <form action="AddMonthlyStatForCompany.php" method="post">
                            <table>
                                <tr><td> ID: </td><td> <input type="text" name="id"> </td></tr>
                                <tr><td> Start Date: </td><td> <input type="date" name="startDate"> </td></tr> 
                                <tr><td> End Date: </td><td> <input type="date" name="endDate"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="numberClass">
                        25.
                    </div>
                    <!-- Add Monthly Statistics To A Certain User -->
                    <div align="center">
                        <h3>Add Monthly Statistics To User:</h3>
                        <form action="AddMonthlyStatForUser.php" method="post">
                            <table>
                                <tr><td> ID: </td><td> <input type="text" name="id"> </td></tr>
                                <tr><td> Start Date: </td><td> <input type="date" name="startDate"> </td></tr> 
                                <tr><td> End Date: </td><td> <input type="date" name="endDate"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        26.
                    </div>
                    <!-- Get All Monthly Statistics Of A Certain Company -->
                    <div align="center">
                        <h3>Get All Statistics Of A Company:</h3>
                        <form action="GetAllMonthlyStatsForCompany.php" method="get">
                            <table>
                                <tr><td> ID: </td><td> <input type="text" name="id"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        27.
                    </div>
                    <!-- Get All Monthly Statistics Of A Certain User -->
                    <div align="center">
                        <h3>Get All Statistics Of A User:</h3>
                        <form action="GetAllMonthlyStatsForUser.php" method="get">
                            <table>
                                <tr><td> ID: </td><td> <input type="text" name="id"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        28.
                    </div>
                    <!-- Update A User Address -->
                    <div align="center">
                        <h3>Update User Address:</h3>
                        <form action="UpdateUserAddress.php" method="post">
                            <table>
                                <tr><td> User ID: </td><td> <input type="text" name="id"> </td></tr>
                                <tr><td> Street: </td><td> <input type="text" name="street"> </td></tr>
                                <tr><td> House Number: </td><td> <input type="text" name="house_number"> </td></tr>
                                <tr><td> City: </td><td> <input type="text" name="city"> </td></tr>
                                <tr><td> Postal Code: </td><td> <input type="text" name="postal_code"> </td></tr>
                                <tr><td> Country: </td><td> <input type="text" name="country"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="numberClass">
                        29.
                    </div>
                    <!-- Update A Company Address -->
                    <div align="center">
                        <h3>Update Company Address:</h3>
                        <form action="UpdateCompanyAddress.php" method="post">
                            <table>
                                <tr><td> Company ID: </td><td> <input type="text" name="id"> </td></tr>
                                <tr><td> Street: </td><td> <input type="text" name="street"> </td></tr>
                                <tr><td> House Number: </td><td> <input type="text" name="house_number"> </td></tr>
                                <tr><td> City: </td><td> <input type="text" name="city"> </td></tr>
                                <tr><td> Postal Code: </td><td> <input type="text" name="postal_code"> </td></tr>
                                <tr><td> Country: </td><td> <input type="text" name="country"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        30.
                    </div>
                    <!-- Create A New Message -->
                    <div align="center">
                       <h3>New Massage:</h3>
                        <form action="NewMassage.php" method="post">
                            <table>
                                <tr><td>From Email: </td><td> <input type="text" name="fromEmail"> </td></tr>
                                <tr><td>From ID: </td><td> <input type="text" name="fromID"> </td></tr>
                                <tr><td>To Email: </td><td> <input type="text" name="toEmail"> </td></tr> 
                                <tr><td>To Email: </td><td> <input type="text" name="toID"> </td></tr> 
                                <tr><td> CC: </td><td> <input type="text" name="cc"> </td></tr> 
                                <tr><td> Subject: </td><td> <input type="text" name="subject"> </td></tr>
                                <tr><td> Content: </td><td> <input type="text" name="content"> </td></tr>
                                <tr><td> Status: </td><td> <select name="status">
                                                                    <option value="sent">sent</option>
                                                                    <option value="failed">failed</option>
                                                                    <option value="pending">pending</option>
                                                                    </select> </td></tr>
                               <tr><td> File Name: </td><td> <input type="text" name="fileName"> </td></tr> 
                               <tr><td> File Path: </td><td> <input type="text" name="filePath"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        31.
                    </div>
                    <!-- Get All Unpaid Invoices Of A Certain Company -->
                    <div align="center">
                        <h3>Get All Unpaid Invoices Of A Company:</h3>
                        <form action="GetCompanyUnpaidInvoices.php" method="get">
                            <table>
                                <tr><td> Company ID: </td><td> <input type="text" name="id"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        32.
                    </div>
                    <!-- Get All Unpaid Invoices Of A Certain User -->
                    <div align="center">
                        <h3>Get All Unpaid Invoices Of A User:</h3>
                        <form action="GetUserUnpaidInvoices.php" method="get">
                            <table>
                                <tr><td> User ID: </td><td> <input type="text" name="id"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="numberClass">
                        33.
                    </div>
                    <!-- Get User Invoices From Date -->
                    <div align="center">
                        <h3>Get User Invoices From Date:</h3>
                        <form action="GetUserInvoiceFromDate.php" method="get">
                            <table>
                                <tr><td> User ID: </td><td> <input type="text" name="id"> </td></tr>
                                <tr><td> Start Date: </td><td> <input type="date" name="fromDate"> </td></tr> 
                                <tr><td> End Date: </td><td> <input type="date" name="toDate"> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
                <td>
                    <div class="numberClass">
                        34.
                    </div>
                    <!-- Create New Invoice For User OR Company -->
                    <div align="center">
                        <h3>Create New Invoice For User OR Company:</h3>
                        <form action="CreateNewInvoice.php" method="post">
                            <table>
                                <tr><td> For: </td><td> <select name="for">
                                                                    <option value="user">user</option>
                                                                    <option value="company">company</option>
                                                                    </select> </td></tr>
                                <tr><td> ID: </td><td> <input type="text" name="id"> </td></tr>
                                <tr><td> Email: </td><td> <input type="text" name="email"> </td></tr>
                                <tr><td> Amount: </td><td> <input type="text" name="amount"> </td></tr>
                                <tr><td> Issue Date: </td><td> <input type="date" name="issue_date"> </td></tr>
                                <tr><td> Due Date: </td><td> <input type="date" name="due_date"> </td></tr>
                                <tr><td> Content: </td><td> <input type="text" name="content"> </td></tr>
                                <tr><td> Is Paid: </td><td> <select name="is_paid">
                                                                    <option value="no">0</option>
                                                                    <option value="yes">1</option>
                                                                    </select> </td></tr>
                            </table>
                        <input type="submit">
                        </form>
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>
