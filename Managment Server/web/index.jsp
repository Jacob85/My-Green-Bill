<%@ page import="com.mygreenbill.common.GreenBillUser" %>
<%--
  Created by IntelliJ IDEA.
  User: Jacob
  Date: 6/21/14
  Time: 11:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <%
        /*if the session contains user mean the user is still logged on -> forward to dashboard*/
        GreenBillUser user = (GreenBillUser) session.getAttribute("user");
        if (user != null && user.isLoggedIn())
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/dashboard"));
    %>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="icon" href="img/favicon.ico" type="image/x-icon">
    <title>My Green Bill</title>

    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/login.css" />
    <link rel="stylesheet" href="assets/plugins/magic/magic.css" />
    <link rel="stylesheet" href="assets/plugins/Font-Awesome/css/font-awesome.min.css" />
    <link rel="stylesheet" href="assets/css/main.min.css" />
    <link rel="stylesheet" href="assets/css/theme.css" />
    <link rel="stylesheet" href="assets/css/MoneAdmin.css" />
    <link rel="stylesheet" href="assets/plugins/social-buttons/social-buttons.css" />
    <link rel="stylesheet" href="css/magnific-popup.css" />
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
    <script src="js/jquery.magnific-popup.min.js"></script>
    <!-- Add my JavaScript to the page -->
    <script src="js/mygreenbill.js"></script>
    <script type="text/javascript">
        (function() {
            var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
            po.src = 'https://apis.google.com/js/client.js?onload=onLoadCallback';
            var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
        })();

        function onLoadCallback()
        {
            gapi.client.load('plus', 'v1',function(){});//Load Google + API
        }
    </script>
    <%
        Object ob = session.getAttribute("passRestore");
        if (ob != null)
        {
            boolean resendEmail = Boolean.parseBoolean(String.valueOf(ob));
            if (resendEmail)
            {
                out.write("<script type=\"text/javascript\" src=\"js/pnotify.custom.min.js\"></script>\n"                        +
                        "<script type=\"text/javascript\"  src=\"js/showNotificationReset.js\"></script>");
            }
        }
    %>
</head>
<!-- NAVBAR
================================================== -->
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
            <a class="navbar-brand" href="#">My Green Bill</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li><a href="contact.jsp">Contact Us</a></li>
            </ul>
            <form id="login_form" class="navbar-form navbar-right" role="form">
                <a data-toggle="modal" data-target="#login_modal" href="#login" class="btn btn-success">Sign In</a>
                <a data-toggle="modal" data-target="#registration_modal" href="#signup" class="btn btn-primary">Sign Up</a>
            </form>
        </div><!--/.navbar-collapse -->
    </div>
</div>

<!-- Carousel
================================================== -->
<div id="myCarousel" class="carousel slide" data-ride="carousel">
    <!-- Indicators -->
    <ol class="carousel-indicators">
        <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
        <li data-target="#myCarousel" data-slide-to="1"></li>
        <li data-target="#myCarousel" data-slide-to="2"></li>
    </ol>
    <div class="carousel-inner">
        <div class="item active">
            <img data-src="holder.js/900x500/auto/#777:#7a7a7a/text:My Green Bill" alt="First slide">
            <div class="container">
                <div class="carousel-caption">
                    <h1>Join Us Today</h1>
                    <p>Register today and start getting your bills by email instead of paper over mail. you can register using google or full registration</p>
                    <img src="img/join_us_bg.jpg" style="width: 60%; top: 40px"><br><br>
                </div>
            </div>
        </div>
        <div class="item">
            <img data-src="holder.js/900x500/auto/#666:#6a6a6a/text:My Green Bill" alt="Second slide">
            <div class="container">
                <div class="carousel-caption">
                    <h1>Got new Email address?</h1>
                    <p>Change your email is easy then ever, just log in to your dashboard and change the email setting, we'll take it from there</p>
                    <img src="img/index_bg.jpg" style="width: 60%; top: 40px">
                </div>
            </div>
        </div>
        <div class="item">
            <img data-src="holder.js/900x500/auto/#666:#6a6a6a/text:My Green Bill" alt="Second slide">
            <div class="container">
                <div class="carousel-caption">
                    <h1>So How to Get Started?</h1>
                    <p>Press the following image to watch a short video on how to use the Application</p>
                    <a class="popup-youtube" href="https://www.youtube.com/watch?v=5XlTmJd99Q4"><img src="img/clip_image.jpg" style="width: 60%; top: 40px"></a>
                </div>
            </div>
        </div>
    </div>
    <a class="left carousel-control" href="#myCarousel" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
    <a class="right carousel-control" href="#myCarousel" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
</div><!-- /.carousel -->



<!-- Marketing messaging and featurettes
================================================== -->
<!-- Wrap the rest of the page in another container to center all the content. -->

<div class="container marketing">

    <!-- Three columns of text below the carousel -->
    <div class="row">
        <div class="col-lg-4">
            <img class="img-circle" src="img/do.png" alt="Generic placeholder image">
            <h2>Don't Panic</h2>
            <p>Managing your bills has never bean so easy! get all your bills in one place - Free archive for your bills - You will never forget to pay another bill again</p>
            <p><a class="btn btn-success" href="#dont_panic" role="button">View details &raquo;</a></p>
        </div><!-- /.col-lg-4 -->
        <div class="col-lg-4">
            <img class="img-circle" src="img/think_green.png" alt="Generic placeholder image">
            <h2>Think Green</h2>
            <p>Start getting your Monthly bills as electronic bills instead of paper over mail! Save paper and money! By saving the paper you will help to save the world</p>
            <p><a class="btn btn-success" href="#save_trees" role="button">View details &raquo;</a></p>
        </div><!-- /.col-lg-4 -->
        <div class="col-lg-4">
            <img class="img-circle" src="img/stats_small.jpg" alt="Generic placeholder image">
            <h2>Personal Analytics</h2>
            <p>Get your Personal analytic! from now tracking your monthly expenses in easy than ever, see where you can save money and what expenses can you reduce <payment></payment></p>
            <p><a class="btn btn-success" href="#stats_big" role="button">View details &raquo;</a></p>
        </div><!-- /.col-lg-4 -->
    </div><!-- /.row -->


    <!-- START THE FEATURETTES -->

    <hr class="featurette-divider">

    <div id="dont_panic" class="row featurette">
        <div class="col-md-7">
            <h2 class="featurette-heading">Don't Panic <span class="text-muted">paying bills was never so easy</span></h2>
            <p class="lead">With "My Green Bill" you can easily manage your monthly expenses! you will have the option to  get all bills in one place, filter, pay, remind and get custom stats!</p>
        </div>
        <div class="col-md-5">
            <img class="featurette-image img-responsive" src="img/dont_panic.jpg" alt="Generic placeholder image">
        </div>
    </div>

    <hr class="featurette-divider">

    <div id="save_trees" class="row featurette">
        <div class="col-md-5">
            <img class="featurette-image img-responsive" src="img/save-trees.jpg" alt="Generic placeholder image">
        </div>
        <div class="col-md-7">
            <h2 class="featurette-heading">Save the Trees <span class="text-muted">Save Money</span></h2>
            <p class="lead">With My Green Bill you can easily get all your monthly bills as electronically over email, save the paper they printed on and make our globe a better place,
                All you need to do is choose from which service providers you wish to get monthly bill as electronic one instead of paper over mail
                We will do all the hard work of informing the company. go back is always possible.
            </p>
        </div>
    </div>

    <hr class="featurette-divider">

    <div id="stats_big" class="row featurette">
        <div class="col-md-7">
            <h2 class="featurette-heading">Custom Analytic <span class="text-muted"></span></h2>
            <p class="lead">
                My Green bill know how to extract the total amount to pay out of your bills, we then construct a custom analytic for your bills
                you can get information by category, service provider and date.
            </p>
        </div>
        <div class="col-md-5">
            <img class="featurette-image img-responsive" src="img/stats_big.jpg" alt="Generic placeholder image">
        </div>
    </div>

    <hr class="featurette-divider">

    <!-- /END THE FEATURETTES -->


    <!-- FOOTER -->
    <footer>
        <p class="pull-right"><a href="#">Back to top</a></p>
        <p>&copy; 2014 My Green Bill, Inc. &middot; </p>
    </footer>

    <!-- Registration Model -->
    <div class="modal fade" id="registration_modal" tabindex="-1" role="dialog" aria-labelledby="registration_label" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="registration_label">Login/Register</h4>
                </div>
                <div class="modal-body">
                    <div class="tab-content">
                        <div id="signup" class="tab-pane">
                            <form action="${pageContext.request.contextPath}/register/full" class="form-signin" id="full_registration_form" method="post">
                                <p class="text-muted text-center btn-block btn btn-primary btn-rect">Please Fill Details To Register</p>
                                <input type="email" placeholder="Your E-mail" class="form-control" id="full_registration_inputEmail" name="full_registration_inputEmail"/>
                                <input type="password" placeholder="password" class="form-control" id="full_registration_inputPassword" name="full_registration_inputPassword"/>
                                <input type="password" placeholder="Re type password" class="form-control" id="full_registration_confirmPassword" name="full_registration_confirmPassword"/>
                                <input type="text" placeholder="Id" class="form-control" id="full_registration_inputId" name="full_registration_inputId" ><br>
                                <button class="btn text-muted text-center btn-success" type="submit">Register</button>
                            </form>
                        </div>
                        <div id="appLogin" class="tab-pane">
                            <form id="appLoginForm" method="post" action="${pageContext.request.contextPath}/register/byApp" class="form-signin">
                                <p class="text-muted text-center btn-block btn btn-primary btn-rect">Enter your valid ID</p>
                                <input type="text"  required="required" placeholder="Your ID"  class="form-control" id="idInput" name="appRegisterId" />
                                <button type="submit" id="googleLoginButton" class="form-control btn btn-block btn-social btn-google-plus" style="top: 10px">
                                    <i class="icon-google-plus"></i> Register in with Google
                                </button>
                            </form>
                        </div>
                        <form class="hidden" id="goggleLoginForm" method="post" action="${pageContext.request.contextPath}/authenticate/loginByApp">

                        </form>
                    </div>

                    <div class="text-center">
                        <ul class="list-inline">
                            <li><a id="toogleSignUp" class="btn" href="#signup" data-toggle="tab">Signup</a></li>
                            <li><a class="btn" href="#appLogin" data-toggle="tab">Google Login</a></li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Full Registration Model -->
    <div class="modal fade" id="login_modal" tabindex="-1" role="dialog" aria-labelledby="login_modal_label" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="login_modal_label">Login</h4>
                </div>
                <div class="modal-body">
                    <div class="tab-content">
                        <div id="login" class="tab-pane active">
                            <form  id="form-signin" class="form-signin" method="post" action="${pageContext.request.contextPath}/authenticate/login">
                                <p class="text-muted text-center btn-block btn btn-primary btn-rect">
                                    Enter your username and password
                                </p>
                                <input type="email" placeholder="Email" class="form-control" name="login_form_email"/>
                                <input type="password" placeholder="Password" class="form-control" name="login_form_password"/>
                                <button class="btn text-muted text-center btn-danger" type="submit">Sign in</button>
                                <a href="#" id="loginWithGoogle" class="btn btn-block btn-social btn-google-plus" style="top: 10px"><i class="icon-google-plus"></i> Sign in with Google</a>
                            </form>                                                                                                                                           </div>
                        <div id="forgot" class="tab-pane">
                            <form method="post" action="${pageContext.request.contextPath}/authenticate/restorePassword" class="form-signin">
                                <p class="text-muted text-center btn-block btn btn-primary btn-rect">Enter your valid e-mail</p>
                                <input type="email"  required="required" placeholder="Your E-mail"  class="form-control" id="email" name="email" />
                                <br />
                                <button class="btn text-muted text-center btn-success" type="submit">Recover Password</button>
                            </form>
                        </div>
                    </div>

                    <div class="text-center">
                        <ul class="list-inline">
                            <li><a class="btn" href="#login" data-toggle="tab">Login</a></li>
                            <li><a class="btn" href="#forgot" data-toggle="tab">Forgot Password</a></li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

</div><!-- /.container -->
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
