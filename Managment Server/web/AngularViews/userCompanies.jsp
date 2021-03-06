<%--
  Created by IntelliJ IDEA.
  User: ipeleg
  Date: 5/13/14
  Time: 9:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mygreenbill.common.GreenBillUser" %>

<%
    // Getting the user object from the current session
    GreenBillUser user = (GreenBillUser) request.getSession().getAttribute("user");
    if (user == null)
    {
        System.out.println(user);
        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/"));
        return;
    }
%>

<link href="assets/css/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/pnotify.custom.min.css" media="all" rel="stylesheet" type="text/css" />

<!-- <script src="assets/plugins/jquery-2.1.1.min.js"></script> -->
<script type="text/javascript" src="js/pnotify.custom.min.js"></script>

<div class="row">
    <div class="col-lg-8">
        <h2><%=user.getFullName()%>'s Companies </h2>
        <a href="#/User/Companies/Select" class="btn btn-primary">Add Companies</a>
    </div>
</div>

<hr />

<div class="row">
    <div ng-repeat="company in companies" class="col-lg-3">
        <div class="panel panel-default">
            <div class="panel-heading">
                {{ company.name }}
                <Button class="icon-remove" style="float: right" ng-click="askToSubmit(company.id, company.name)"></Button>
            </div>
            <div class="panel-body">
                <img class="company-logo" ng-src={{company.logo_path}} />
            </div>
        </div>
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
