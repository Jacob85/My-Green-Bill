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
    <script src="assets/css/jquery-ui.css"></script>
    <link href="css/pnotify.custom.min.css" media="all" rel="stylesheet" type="text/css" />

    <script src="assets/plugins/jquery-2.0.3.min.js"></script>
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>


    <title>Success</title>

    <%
        Object ob = session.getAttribute("resendEmail");
        if (ob != null)
        {
            boolean resendEmail = Boolean.parseBoolean(String.valueOf(ob));
            if (resendEmail)
            {
               out.write("<script type=\"text/javascript\" src=\"js/pnotify.custom.min.js\"></script>\n"
                       +
                         "<script type=\"text/javascript\"  src=\"js/showNotification.js\"></script>");
            }
        }

    %>
</head>
<body>
  <%
      GreenBillUser greenBillUser = (GreenBillUser) request.getSession().getAttribute("user");
  %>
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
  <br>
  <br>
  <br>
  <div class="container">

      <!-- Main hero unit for a primary marketing message or call to action -->
      <div class="jumbotron">
        <h2><%= "Hello " + greenBillUser.getFullName()%></h2>
         <p>To Active your account and access Dashboard please press the link on the email sent to you <br>
            The email was sent to <%=greenBillUser.getForwardEmail()%></p>
          <p>
              If you did not get the email yet please check your spam folder <br>
              To resend the activation email please press <b><a href="${pageContext.request.contextPath}/authenticate/resendActivationEmail">here</a></b>
          </p>
      </div>
  </div>
  <script>
      (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
          (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
              m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
      })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

      ga('create', 'UA-52561351-1', 'auto');
      ga('send', 'pageview');

  </script>
</body>

</html>
