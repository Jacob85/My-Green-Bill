<%--
  Created by IntelliJ IDEA.
  User: ipeleg
  Date: 5/13/14
  Time: 9:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mygreenbill.common.GreenBillUser" %>

    <script>
        console.log( "document test" );
        $(document).ready(function()
        {
            console.log( "document loaded" );
        });
    </script>
    <%
        // Getting the user object from the current session
        GreenBillUser user = (GreenBillUser) request.getSession().getAttribute("user");
    %>
    <div class="row">
        <div class="col-lg-8">
            <h2>THIS IS USER COMPANIES PAGE</h2>
            <a href="#/User/Companies/Select" class="btn btn-primary">Add Company</a>
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
