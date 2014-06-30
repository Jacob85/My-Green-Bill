<%--
  Created by IntelliJ IDEA.
  User: Jacob
  Date: 6/30/14
  Time: 11:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="icon" href="img/favicon.ico" type="image/x-icon">
    <title>Contact us</title>

    <!-- Bootstrap core CSS -->
    <link href='http://fonts.googleapis.com/css?family=Roboto:400,300,700italic,700,500&amp;subset=latin,latin-ext' rel='stylesheet' type='text/css'>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/login.css" />
    <link rel="stylesheet" href="assets/plugins/magic/magic.css" />
    <link rel="stylesheet" href="assets/plugins/Font-Awesome/css/font-awesome.min.css" />
    <link rel="stylesheet" href="assets/css/main.min.css" />
    <link rel="stylesheet" href="assets/css/theme.css" />
    <link rel="stylesheet" href="assets/css/MoneAdmin.css" />
    <link rel="stylesheet" href="assets/plugins/social-buttons/social-buttons.css" />
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- Custom styles for this template -->
    <link href="css/carousel.css" rel="stylesheet">


    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/docs.min.js"></script>
    <!-- Form Validation Script -->
    <script src="js/jquery.validate.min.js"></script>
    <!-- Add my JavaScript to the page -->
    <script src="js/mygreenbill.js"></script>
    <link href="css/contact.css" rel="stylesheet">
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
            <a class="navbar-brand" href="index.jsp">My Green Bill</a>
        </div>
        <div class="navbar-collapse collapse">
        </div><!--/.navbar-collapse -->
    </div>
</div>

<!-- Main hero unit for a primary marketing message or call to action -->
<header>
<div class="jumbotron">

    <div class="container">
        <div class="row">
            <div class="col-sm-12 col-lg-12">
                <%
                    Object ob = session.getAttribute("contactUs");
                    if (ob != null)
                    {
                        out.write("<h1>Thank You! <small>Your feedback was sent, one of our representative will contact you shortly..</small></h1>");
                        session.removeAttribute("contactUs");
                    }
                    else
                    {
                        out.write("<h1>Stay In Touch <small>feel free to contact us for any Inquiry</small></h1>");
                    }
                %>
            </div>
        </div>
    </div>
</div>
</header>
<div class="container">
    <!-- Example row of columns -->
    <div class="row">
        <div class="col-sm-12 col-lg-12">
            <div class="panel">
                <div class="panel-heading">
                    <h3><i class="icon-map-marker main-color"></i> Location</h3>
                </div>
                <div class="panel-body">
                    <!-- gMap script container !Do not remove!! -->
                    <div id="map"></div>
                    <!-- gMap script container !Do not remove!! -->
                </div>
            </div>
        </div>
    </div>
    <hr>
    <div class="row">
        <div class="col-sm-4 col-lg-4">
            <div class="panel">
                <div class="panel-heading">
                    <h3><i class="icon-pushpin main-color"></i> Our office</h3>
                </div>
                <div class="panel-body">
                    <address>
                        <strong>My Green Bill, Inc.</strong><br>
                        13 A Brandis Streat<br>
                        Ra'anana Israel<br>
                        <i class="icon-phone-sign"></i> + 972 (50) 9049-749
                    </address>
                </div>
            </div>

            <div class="panel">
                <div class="panel-heading">
                    <h3><i class="icon-time main-color"></i> Business hours</h3>
                </div>
                <div class="panel-body">
                    <table class="table table-hover">
                        <thead>
                        <tr>

                            <th>Day</th>
                            <th>Time</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr class="success">
                            <td>Sunday</td>
                            <td>9:00 to 18:00</td>
                        </tr>
                        <tr class="success">
                            <td>Monday</td>
                            <td>9:00 to 18:00</td>
                        </tr>
                        <tr class="success">
                            <td>Tuesday</td>
                            <td>9:00 to 18:00</td>
                        </tr>
                        <tr class="success">
                            <td>Wednesday</td>
                            <td>9:00 to 18:00</td>
                        </tr>
                        <tr class="success">
                            <td>Thursday</td>
                            <td>9:00 to 18:00</td>
                        </tr>
                        <tr class="warning">
                            <td>Friday</td>
                            <td>9:00 to 14:00</td>
                        </tr>
                        <tr class="danger">
                            <td>Saturday</td>
                            <td>day off</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-12 col-lg-8">

            <div class="panel">
                <div class="panel-heading">
                    <h3 class="">
                        <i class="icon-envelope main-color"></i>
                        Feel free to contact us
                    </h3>
                </div>
                <div class="panel-body">
                    <!-- CONTACT FORM https://github.com/jonmbake/bootstrap3-contact-form -->

                    <form role="form" id="feedbackForm" method="post" action="${pageContext.request.contextPath}/authenticate/contactUs">
                        <div class="form-group">
                            <input type="text" class="form-control" id="name" name="name" placeholder="Name">
                            <span class="help-block" style="display: none;">Please enter your name.</span>
                        </div>
                        <div class="form-group">
                            <input type="email" class="form-control" id="email" name="email" placeholder="Email Address">
                            <span class="help-block" style="display: none;">Please enter a valid e-mail address.</span>
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" id="title" name="title" placeholder="Title">
                            <span class="help-block" style="display: none;">Please enter Title.</span>
                        </div>
                        <div class="form-group">
                            <textarea rows="10" cols="100" class="form-control" id="message" name="message" placeholder="Message"></textarea>
                            <span class="help-block" style="display: none;">Please enter a message.</span>
                        </div>
                        <button type="submit" id="feedbackSubmit" class="btn btn-primary btn-lg" style="display: block; margin-top: 10px;">Send Feedback</button>
                    </form>
                    <!-- END CONTACT FORM -->
                </div>
            </div>
        </div>
    </div>

    <hr>

    <footer>
        <div class="container">
            <div class="row">
                <div class="col-12 col-lg-12">
                    <p><a href="http://www.mygreenbill.com" title="My Green Bill">mygreenbill.com © Company 2014</a></p>
                </div>
            </div>
        </div>
    </footer>

</div> <!-- /container -->

<!-- Le javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<%--<script src="assets/js/jquery.js" type="text/javascript"></script>
<!-- Latest compiled and minified JavaScript -->
<script src="assets/js/bootstrap.min.js"></script>

<script src="assets/js/contact-form.js"></script>--%>



<!-- gMap PLUGIN -->
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="js/jquery.gmap.js"></script>


<script type="text/javascript">

    jQuery(document).ready(function(){

        jQuery('#map').gMap({ address: 'Raanana',
            panControl: true,
            zoomControl: true,
            zoomControlOptions: {
                style: google.maps.ZoomControlStyle.SMALL
            },
            mapTypeControl: true,
            scaleControl: true,
            streetViewControl: false,
            overviewMapControl: true,
            scrollwheel: true,
            icon: {
                image: "http://www.google.com/mapfiles/marker.png",
                shadow: "http://www.google.com/mapfiles/shadow50.png",
                iconsize: [20, 34],
                shadowsize: [37, 34],
                iconanchor: [9, 34],
                shadowanchor: [19, 34]
            },
            zoom:14,
            markers: [
                { 'address' : 'Raanana'}
            ]


        });
    });
</script>
</body>
</html>
