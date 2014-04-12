<%@ page import="com.mygreenbill.common.Question" %>
<%--
  Created by IntelliJ IDEA.
  User: Jacob
  Date: 4/12/14
  Time: 5:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Validate the questions</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>

    <!-- Form Validation Script -->
    <script src="${pageContext.request.contextPath}/js/jquery.validate.min.js"></script>
    <!-- Add my JavaScript to the page -->
    <script src="${pageContext.request.contextPath}/js/mygreenbill.js"></script>



    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</head>
<body>
    <%
        String firstQuestion = (String) request.getSession().getAttribute("question1");
        String secondQuestion = (String) request.getSession().getAttribute("question2");
    %>
    <form class="form-horizontal" method="post" action="${pageContext.request.contextPath}/register/full">
        <div class="form-group">
            <label for="first_validation_question" class="control-label col-xs-2"><%=firstQuestion %></label>
            <div class="col-xs-10">
                <input type="text" class="form-control" id="first_validation_question" name="first_validation_question" placeholder="First Question">
            </div>
        </div>
        <div class="form-group">
            <label for="second_validation_question" class="control-label col-xs-2"><%=secondQuestion %></label>
            <div class="col-xs-10">
                <input type="text" class="form-control" id="second_validation_question" name="second_validation_question" placeholder="Second Question">
            </div>
        </div>
    </form>
</body>
</html>
