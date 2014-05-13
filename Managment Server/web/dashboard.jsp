<%@ page import="java.util.Properties" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="com.mygreenbill.common.GreenBillUser" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%--
  Created by IntelliJ IDEA.
  User: Jacob
  Date: 5/5/14
  Time: 11:31 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html ng-app="app">
<head>
    <meta charset="UTF-8" />
    <%
        Properties properties = new Properties();
        //todo yaki - replace with the relative path on the server
        FileInputStream inputStream = new FileInputStream("/Users/ipeleg/IdeaProjects/My-Green-Bill/Managment Server/web/WEB-INF/properties/dashboard.properties");
        //FileInputStream inputStream = new FileInputStream("C:\\Users\\Jacob\\IdeaProjects\\My Green Bill\\Managment Server\\web\\WEB-INF\\properties\\dashboard.properties");
        properties.load(inputStream);
    %>
    <title><%=properties.getProperty("title")%></title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <meta content="" name="description" />
    <meta content="" name="author" />
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <![endif]-->
    <!-- GLOBAL STYLES -->
    <link rel="stylesheet" href="assets/plugins/bootstrap/css/bootstrap.css" />
    <link rel="stylesheet" href="assets/css/main.css" />
    <link rel="stylesheet" href="assets/css/theme.css" />
    <link rel="stylesheet" href="assets/css/MoneAdmin.css" />
    <link rel="stylesheet" href="assets/plugins/Font-Awesome/css/font-awesome.css" />
    <link rel="stylesheet" href="css/style.css" />
    <!--END GLOBAL STYLES -->

    <!-- PAGE LEVEL STYLES -->
    <link href="assets/css/layout2.css" rel="stylesheet" />
    <link href="assets/plugins/flot/examples/examples.css" rel="stylesheet" />
    <link rel="stylesheet" href="assets/plugins/timeline/timeline.css" />
    <!-- END PAGE LEVEL  STYLES -->
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->

</head>
<body class="padTop53">

    <div id="wrap">
        <div id="top">
            <nav class="navbar navbar-inverse navbar-fixed-top" style="padding-top: 10px">
                <!-- LOGO SECTION -->
                <header class="navbar-header">
                    <a href="#" class="navbar-brand brand" style="color: limegreen">
                        <%=properties.getProperty("brand")%>
                    </a>
                </header>

                <ul class="nav navbar-top-links navbar-right">

                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                            <i class="icon-user "></i>&nbsp; <i class="icon-chevron-down "></i>
                        </a>

                        <ul class="dropdown-menu dropdown-user">
                            <li><a href="#"><i class="icon-user"></i><%=properties.getProperty("user_profile")%> </a> </li>
                            <li class="divider"></li>
                            <li><a href="${pageContext.request.contextPath}/authenticate/logout"><i class="icon-signout"></i><%=properties.getProperty("logout")%> </a></li>
                        </ul>
                    </li>
                </ul>
            </nav>
        </div>

        <%
            //todo yaki - remove the demo user and take the data from the ssesion only
            GreenBillUser user = (GreenBillUser) session.getAttribute("user");
            user = new GreenBillUser("Idan", "Peleg", "038054664", "decececec", "ipeleg@gmail.com", new ArrayList<Integer>(5));


        %>
        <!-- MENU SECTION -->
        <div id="left" >
        <div class="media user-media well-small">
            <a class="user-link" href="#">
                <img class="media-object img-thumbnail user-img" alt="User Picture" src="assets/img/user.gif" />
            </a>
            <br />
            <div class="media-body">
                <h5 class="media-heading"> <%=user.getFirstName() + " " + user.getLastName()%></h5>
                <ul class="list-unstyled user-info">
                    <li>
                        <a class="btn btn-success btn-xs btn-circle" style="width: 10px;height: 12px;"></a> Online
                    </li>
                </ul>
            </div>
            <br />
        </div>

        <ul id="menu" class="collapse">


        <li class="panel active">
            <a href="#" >
                <i class="icon-table"></i><%=properties.getProperty("dashboard")%></a>
        </li>

        <li class="panel ">
            <a href="" data-parent="#menu" data-toggle="collapse" class="accordion-toggle" data-target="#component-nav">
                <i class="icon-tasks"> </i> <%=properties.getProperty("my_bills")%>
                        <span class="pull-right">
                          <i class="icon-angle-left"></i>
                        </span>
                &nbsp; <span class="label label-success"><%=/*user.getUserCompanyList().size()*/ 5%></span>&nbsp;
            </a>
            <ul class="collapse" id="component-nav">
                <%
                    String[] companies = new String[]{"Bezeq", "Paz Gaz", "Migdal", "Harel", "Orange", "Raanana Water"};
                    for(String company : companies)
                    {
                        out.write("<li class=\"\"><a href=\"#\"><i class=\"icon-angle-right\"></i> " + company +"</a></li>");
                    }
                %>
            </ul>
        </li>

        <li class="panel">
            <a href="" data-parent="#menu" data-toggle="collapse" class="accordion-toggle" data-target="#user_profile_nav">
                <i class="icon-user"></i> <%= properties.getProperty("user_profile")%>
            </a>
            <ul class="collapse" id="user_profile_nav">
                <%
                    // Key = Attribute title, Value = Attribute path
                    HashMap<String, String> attributes = new HashMap<String, String>();
                    attributes.put("My Companies", "/User/Companies");
                    attributes.put("Account Settings", "/User/Settings");

                    for(Map.Entry<String, String> entry : attributes.entrySet())
                    {
                        out.write("<li><a href='#" + entry.getValue() + "'><i class='icon-angle-right'></i> " + entry.getKey() +"</a></li>");
                    }
                %>
            </ul>
        </li>

        <li>
            <a href="#/User/Statistics"><i class="icon-bar-chart"></i> <%= properties.getProperty("statistics")%></a>
        </li>

        </ul>

        </div>
        <!--END MENU SECTION -->

        <div id="content">

            <div class="inner">

                <!-- Place holder for the views -->
                <div ng-view="">

                </div>
            </div>
        </div>
    <%--end wrap div --%>
    </div>
    <!-- GLOBAL SCRIPTS -->
    <script src="assets/plugins/jquery-2.0.3.min.js"></script>
    <script src="assets/plugins/bootstrap/js/bootstrap.min.js"></script>
    <script src="assets/plugins/modernizr-2.6.2-respond-1.1.0.min.js"></script>

    <script src="assets/js/angular.min.js"></script>
    <script src="assets/js/angular-route.min.js"></script>
    <!-- END GLOBAL SCRIPTS -->

    <!-- PAGE LEVEL SCRIPTS -->
    <script src="assets/plugins/flot/jquery.flot.js"></script>
    <script src="assets/plugins/flot/jquery.flot.resize.js"></script>
    <script src="assets/plugins/flot/jquery.flot.time.js"></script>
    <script  src="assets/plugins/flot/jquery.flot.stack.js"></script>
    <script src="assets/js/for_index.js"></script>
    <script src="assets/plugins/morris/raphael-2.1.0.min.js"></script>
    <script src="assets/plugins/morris/morris.js"></script>
    <script src="assets/plugins/morris/morris-demo.js"></script>

    <script src="assets/AngularJS/app.js"></script>
    <script src="assets/AngularJS/controllers/UserProfileController.js"></script>
    <script src="assets/AngularJS/controllers/UserStatisticsController.js"></script>
    <script src="assets/AngularJS/controllers/UserDashboardController.js"></script>

    <!-- END PAGE LEVEL SCRIPTS -->
</body>
</html>
