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
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <![endif]-->
    <script src="assets/css/jquery-ui.css"></script>
    <link href="css/pnotify.custom.min.css" media="all" rel="stylesheet" type="text/css" />

    <script src="assets/plugins/jquery-2.0.3.min.js"></script>
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">


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
<body >

<!--  PAGE CONTENT -->
<div class="container">
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
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li><a href="contact.jsp">Contact Us</a></li>
                </ul>
            </div>
        </div>
    </div>
    <br>
    <br>
    <div class="col-lg-8 col-lg-offset-2 text-center">
        <div class="logo">
            <h1>Error :( </h1>          </div>
        <p class="lead text-muted"><%= session.getAttribute("message") %></p>
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

  <%
      session.invalidate();
  %>
<script>
    (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
        (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
            m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
    })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

    ga('create', 'UA-52561351-1', 'auto');
    ga('send', 'pageview');

</script>
</body>
<!-- END BODY -->
</html>
