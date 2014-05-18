<%@ page import="com.mygreenbill.common.GreenBillUser" %>
<%--
  Created by IntelliJ IDEA.
  User: ipeleg
  Date: 5/14/14
  Time: 12:23 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%GreenBillUser greenbillUser = (GreenBillUser) request.getSession().getAttribute("user");%>
    <title></title>
</head>
<body>
    <div class="row">
        <div class="col-lg-12">
            <h2>THIS IS USER DASHBOARD PAGE</h2>
        </div>
    </div>

    <hr />
</body>
</html>
