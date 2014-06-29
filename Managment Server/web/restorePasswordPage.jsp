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
<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${pageContext.request.contextPath}">My Green Bill</a>
        </div>
    </div>
</div>
<br>
<br>
<br>
<br>
<br>
<div class="container">

    <!-- Main hero unit for a primary marketing message or call to action -->
    <div class="jumbotron">
        <h3>Please enter the new password </h3>
        <form id="passwordRestoreForm" class="form-horizontal" method="post" action="${pageContext.request.contextPath}/authenticate/restorePasswordSubmitted">
            <div class="form-group">
                <label for="password" class="control-label col-xs-2">Password</label>
                <div class="col-xs-10">
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                </div>
            </div>
            <div class="form-group">
                <label for="reenteredPassword" class="control-label col-xs-2">Re-enter Password</label>
                <div class="col-xs-10">
                    <input type="password" class="form-control" id="reenteredPassword" name="reenteredPassword" placeholder="Re-enter Password">
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-offset-2 col-xs-10">
                    <button type="submit" class="btn btn-primary btn-block">Submit</button>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>