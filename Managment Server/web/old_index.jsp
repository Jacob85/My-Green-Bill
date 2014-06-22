<%@ page import="com.mygreenbill.common.GreenBillUser" %>
<%-- Created by IntelliJ IDEA. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png" href="img/logo.png" />
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>My Green Bill :)</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/login.css" />
    <link rel="stylesheet" href="assets/plugins/magic/magic.css" />
    <link rel="stylesheet" href="assets/plugins/Font-Awesome/css/font-awesome.min.css" />
    <link rel="stylesheet" href="assets/css/main.min.css" />
    <link rel="stylesheet" href="assets/css/theme.css" />
    <link rel="stylesheet" href="assets/css/MoneAdmin.css" />
    <link rel="stylesheet" href="assets/plugins/social-buttons/social-buttons.css" />




    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>

    <!-- Form Validation Script -->
    <script src="js/jquery.validate.min.js"></script>
    <!-- Add my JavaScript to the page -->
    <script src="js/mygreenbill.js"></script>



    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>

    <%-- Google Sign in--%>
   <%-- <meta name="google-signin-clientid" content="288366509317-1hgkph86vukk0iu4983vsol50b24l0bg.apps.googleusercontent.com" />
    <meta name="google-signin-scope" content="https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email" />
    <meta name="google-signin-requestvisibleactions" content="http://schemas.google.com/AddActivity" />
    <meta name="google-signin-cookiepolicy" content="single_host_origin" />
    <meta name="google-signin-callback" content="signinCallback1" />--%>
    <script type="text/javascript">
        (function() {
            var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
            po.src = 'https://apis.google.com/js/client.js?onload=onLoadCallback';
            var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
        })();

        function onLoadCallback()
        {
            //gapi.client.setApiKey('VMiZxsaNID-iZP3r1jsxomaf'); //set your API KEY
            gapi.client.load('plus', 'v1',function(){});//Load Google + API
        }

/*        function mylogin()
        {
            var myParams = {
                'clientid' : '288366509317-1hgkph86vukk0iu4983vsol50b24l0bg.apps.googleusercontent.com', //You need to set client id
                'cookiepolicy' : 'single_host_origin',
                'callback' : 'loginCallback', //callback function
                'approvalprompt':'force',
                'scope' : 'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email'
            };
            gapi.auth.signIn(myParams);
        }*/

      /*  function loginCallback(authResult)
        {
                if (authResult['status']['signed_in']) {
                    // Update the app to reflect a signed in user
                    // Hide the sign-in button now that the user is authorized, for example:
                    //document.getElementById('signinButton').setAttribute('style', 'display: none');
                    console.log("Success!!");
                    $.getJSON("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + authResult["access_token"], function (data){
                        console.log(data);
                        console.log(data.email);
                    });
                } else {
                    // Update the app to reflect a signed out user
                    // Possible error values:
                    //   "user_signed_out" - User is signed-out
                    //   "access_denied" - User denied access to your app
                    //   "immediate_failed" - Could not automatically log in the user
                    console.log('Sign-in state: ' + authResult['error']);
                }
            console.log(authResult);
        }*/
    </script>

    <%
        Object ob = session.getAttribute("passRestore");
        if (ob != null)
        {
            boolean resendEmail = Boolean.parseBoolean(String.valueOf(ob));
            if (resendEmail)
            {
                out.write("<script type=\"text/javascript\" src=\"js/pnotify.custom.min.js\"></script>\n"
                        +
                        "<script type=\"text/javascript\"  src=\"js/showNotificationReset.js\"></script>");
            }
        }

    %>
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
            <a class="navbar-brand" href="#">My Green Bill</a>
        </div>
        <div class="navbar-collapse collapse">
            <form id="login_form" class="navbar-form navbar-right" role="form">
                <a data-toggle="modal" data-target="#full_registration" href="#login" class="btn btn-success">Sign In</a>
                <a data-toggle="modal" data-target="#full_registration" href="#signup" class="btn btn-primary">Sign Up</a>
            </form>
        </div><!--/.navbar-collapse -->
    </div>
</div>
<!-- Full Registration Model -->
<div class="modal fade" id="full_registration" tabindex="-1" role="dialog" aria-labelledby="full_registration_label" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="full_registration_label">Login/Register</h4>
            </div>
            <div class="modal-body">
                <div class="tab-content">
                    <div id="login" class="tab-pane active">
                        <form  class="form-signin" method="post" action="${pageContext.request.contextPath}/authenticate/login">
                            <p class="text-muted text-center btn-block btn btn-primary btn-rect">
                                Enter your username and password
                            </p>
                            <input type="text" placeholder="Username" class="form-control" name="login_form_email"/>
                            <input type="password" placeholder="Password" class="form-control" name="login_form_password"/>
                            <button class="btn text-muted text-center btn-danger" type="submit">Sign in</button>
                        </form>
                    </div>
                    <div id="forgot" class="tab-pane">
                        <form method="post" action="${pageContext.request.contextPath}/authenticate/restorePassword" class="form-signin">
                            <p class="text-muted text-center btn-block btn btn-primary btn-rect">Enter your valid e-mail</p>
                            <input type="email"  required="required" placeholder="Your E-mail"  class="form-control" id="email" name="email" />
                            <br />
                            <button class="btn text-muted text-center btn-success" type="submit">Recover Password</button>
                        </form>
                    </div>
                    <div id="signup" class="tab-pane">
                        <form action="${pageContext.request.contextPath}/register/full" class="form-signin" id="full_registration_form" method="post">
                            <p class="text-muted text-center btn-block btn btn-primary btn-rect">Please Fill Details To Register</p>
                            <input type="email" placeholder="Your E-mail" class="form-control" id="full_registration_inputEmail" name="full_registration_inputEmail"/>
                            <input type="password" placeholder="password" class="form-control" id="full_registration_inputPassword" name="full_registration_inputPassword"/>
                            <input type="password" placeholder="Re type password" class="form-control" id="full_registration_confirmPassword" name="full_registration_confirmPassword"/>
                            <input type="text" placeholder="Id" class="form-control" id="full_registration_inputId" name="full_registration_inputId" >
                            <button class="btn text-muted text-center btn-success" type="submit">Register</button>
                        </form>
                    </div>
                    <div id="appLogin" class="tab-pane">
                        <form id="appLoginForm" method="post" action="${pageContext.request.contextPath}" class="form-signin">
                            <p class="text-muted text-center btn-block btn btn-primary btn-rect">Enter your valid ID</p>
                            <input type="text"  required="required" placeholder="Your ID"  class="form-control" id="idInput" name="appRegisterId" />
                            <div class="text-muted text-center btn-block" data-toggle="buttons">
                                <label class="btn btn-primary active">
                                    <input type="radio" checked required="required" name="options" placeholder="Log in" class="btn btn-info" value="/authenticate/loginByApp" id="loginRadioBox"> Login
                                </label>
                                <label class="btn btn-primary">
                                    <input type="radio" required="required" name="options" placeholder="Register" class="btn btn-info" value="/register/byApp" id="registerRadioBox"> Register
                                </label>
                            </div>
                            <br>
                            <button type="submit" id="googleLoginButton" class="btn btn-block btn-social btn-google-plus">
                                <i class="icon-google-plus"></i> Sign in with Google
                            </button>
                        </form>
                    </div>
                </div>

                <div class="text-center">
                    <ul class="list-inline">
                        <li><a class="btn" href="#login" data-toggle="tab">Login</a></li>
                        <li><a class="btn" href="#forgot" data-toggle="tab">Forgot Password</a></li>
                        <li><a class="btn" href="#signup" data-toggle="tab">Signup</a></li>
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

<br>
<br>
<br>
<br>
<br>

<%--<input type="button"  value="Login" onclick="mylogin()" />
<input type="button"  value="Logout" onclick="logout()" />--%>
<div class="container">

    <!-- Main hero unit for a primary marketing message or call to action -->
    <div class="jumbotron">
        <h1>Join Us Today!</h1>
        <p id="marketing_text"> The text will come from the javascript </p>
        <p><a href="#" class="btn btn-primary btn-large">Learn more &raquo;</a></p>
    </div>

    <!-- Example row of columns -->
    <div class="row">
        <div class="span4">
            <h2>Heading</h2>
            <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
            <p><a class="btn" href="#">View details &raquo;</a></p>
        </div>
        <div class="span4">
            <h2>Heading</h2>
            <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
            <p><a class="btn" href="#">View details &raquo;</a></p>
        </div>
        <div class="span4">
            <h2>Heading</h2>
            <p>Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.</p>
            <p><a class="btn" href="#">View details &raquo;</a></p>
        </div>
    </div>

    <hr>

    <footer>
        <p>&copy; Company 2013</p>
    </footer>
  </div>

<div id="main">
    <div data-page-name="Home" class="section-unit hero">
        <div class="container">
            <div class="row-fluid">
                <h1>Clean up your inbox</h1>

                <h2 class="followon">Save time on email with Unroll.me <a href="/video">
                    Watch our video</a></h2><a href="/signup" data-track=":Signup|Signup" class=
                    "btn btn-primary icon-chevron-right">Get started now</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>