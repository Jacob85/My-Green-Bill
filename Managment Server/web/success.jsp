<%@ page import="com.mygreenbill.common.GreenBillUser" %>
<%--
  Created by IntelliJ IDEA.
  User: Jacob
  Date: 4/15/14
  Time: 9:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Success</title>
</head>
<body>
  <%
      GreenBillUser greenBillUser = (GreenBillUser) request.getSession().getAttribute("user");
  %>
    <%= "Hello " + greenBillUser.getFirstName() + " " + greenBillUser.getLastName()%>
</body>
</html>
