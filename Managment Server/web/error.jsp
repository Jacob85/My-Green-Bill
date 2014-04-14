<%--
  Created by IntelliJ IDEA.
  User: Jacob
  Date: 4/11/14
  Time: 1:29 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
    <p>
        <%= request.getSession().getAttribute("message") %>
    </p>
</body>
</html>
