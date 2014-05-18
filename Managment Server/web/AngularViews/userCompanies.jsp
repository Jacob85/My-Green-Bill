<%--
  Created by IntelliJ IDEA.
  User: ipeleg
  Date: 5/13/14
  Time: 9:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mygreenbill.common.GreenBillUser" %>

<html>
<head>
    <title></title>

    <!-- Bootstrap -->
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/style.css" rel="stylesheet">

    <script src="../js/bootstrap.min.js"></script>
</head>
<body>
    <%
        // Getting the user object from the current session
        GreenBillUser user = (GreenBillUser) request.getSession().getAttribute("user");
        System.out.println(user.getUserCompanyList());
    %>
    <div class="row">
        <div class="col-lg-8">
            <h2>THIS IS USER COMPANIES PAGE</h2>
            <a data-toggle="modal" data-target="#add_company_dialog" class="btn btn-primary">Add Company</a>
        </div>
    </div>

    <hr />

    <div class="row">
        <div ng-repeat="company in companies" class="col-lg-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    {{ company.name }}
                </div>
                <div class="panel-body">
                    {{ company.id }}
                </div>
            </div>
        </div>
    </div>

    <!-- Add Company Modal -->
    <div class="modal fade" id="add_company_dialog" role="dialog" aria-labelledby="add_company_label" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">

                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="add_company_label">Add Company</h4>
                </div>

                <div class="modal-body">

                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>

            </div>
        </div>
    </div>

</body>
</html>
