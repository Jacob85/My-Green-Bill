<%@ page import="java.net.URI" %>
<%--
  Created by IntelliJ IDEA.
  User: Jacob
  Date: 4/11/14
  Time: 1:29 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->

<!-- BEGIN HEAD -->
<head>
    <meta charset="UTF-8" />
    <title>Error Page</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <meta content="" name="description" />
    <meta content="" name="author" />
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <![endif]-->
    <!-- GLOBAL STYLES -->
    <!-- GLOBAL STYLES -->
    <link rel="stylesheet" type="text/css" href="assets/plugins/bootstrap/css/bootstrap.css" />
    <link rel="stylesheet" type="text/css" href="assets/plugins/Font-Awesome/css/font-awesome.css" />
    <!--END GLOBAL STYLES -->

    <!-- PAGE LEVEL STYLES -->
    <link rel="stylesheet" href="assets/css/error.css" />
    <link rel="stylesheet" href="assets/plugins/magic/magic.css" />
    <!--END PAGE LEVEL STYLES -->
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body >

<!--  PAGE CONTENT -->
<div class="container">
    <div class="col-lg-8 col-lg-offset-2 text-center">
        <div class="logo">
            <h1>Error :( </h1>          </div>
        <p class="lead text-muted"><%= request.getSession().getAttribute("message") %></p>
        <p class="lead text-muted"><%= exception != null ? exception.getMessage(): "" %></p>
        <div class="clearfix"></div>
        <br />
        <div class="col-lg-6  col-lg-offset-3">
            <div class="btn-group btn-group-justified">
                <a href="${pageContext.request.contextPath}" class="btn btn-primary">Return To Home Page</a>
            </div>
        </div>
    </div>
</div>
<!-- END PAGE CONTENT -->



</body>
<!-- END BODY -->
</html>
