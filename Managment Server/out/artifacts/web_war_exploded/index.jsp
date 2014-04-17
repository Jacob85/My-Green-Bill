<%-- Created by IntelliJ IDEA. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>My Green Bill :)</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">

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
            <form id="login_form" class="navbar-form navbar-right" role="form" method="post" action="${pageContext.request.contextPath}/authenticate/login">
                <div class="form-group">
                    <input id="login_form_email" type="email" name="login_form_email" placeholder="login_form_email" class="form-control">
                </div>
                <div class="form-group">
                    <input id="login_form_password" type="password" name="login_form_password" placeholder="login_form_password" class="form-control">
                </div>
                <button type="submit" class="btn btn-success" onsubmit="return validateLoginForm()">Sign In</button>
                <a data-toggle="modal" data-target="#full_registration" class="btn btn-primary">Sign Up</a>
            </form>
        </div><!--/.navbar-collapse -->
    </div>
</div>

<%-- canceled at the moment - the default model will be full regster with butons to google and facebook login --%>
<%--
<!-- Sign up Popup -->
<div class="modal fade" id="sign_up_model" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Sign Up For My Green Bill Service</h4>
            </div>
            <div class="modal-body">
                <p>
                    Welcome to My Green Bill registration form
                    You may register using Facebook, Google or Full Registration
                </p>
                <button class="btn btn-primary">Use Facebook</button>
                <br/>
                <button class="btn btn-primary">Use Google</button>
                <br/>
                <button class="btn btn-primary" data-dismiss="modal" onclick="fullRegistration()">Use Full Registration</button>
                <br/>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>--%>


<!-- Full Registration Model -->
<div class="modal fade" id="full_registration" tabindex="-1" role="dialog" aria-labelledby="full_registration_label" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="full_registration_label">Full Registration</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" method="post" id="full_registration_form" action="${pageContext.request.contextPath}/register/full" onsubmit="return validateFullRegistrationForm()">
                    <div class="form-group">
                        <label for="full_registration_inputEmail" class="control-label col-xs-2">Email</label>
                        <div class="col-xs-10">
                            <input type="email" class="form-control" id="full_registration_inputEmail" name="full_registration_inputEmail" placeholder="Email">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="full_registration_inputPassword" class="control-label col-xs-2">Password</label>
                        <div class="col-xs-10">
                            <input type="password" class="form-control" id="full_registration_inputPassword" name="full_registration_inputPassword" placeholder="Password">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="full_registration_confirmPassword" class="control-label col-xs-2">Confirm Password</label>
                        <div class="col-xs-10">
                            <input type="password" class="form-control" id="full_registration_confirmPassword" name="full_registration_confirmPassword" placeholder="Confirm Password">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="full_registration_inputId" class="control-label col-xs-2">ID</label>
                        <div class="col-xs-10">
                            <input type="text" class="form-control" id="full_registration_inputId" name="full_registration_inputId" placeholder="Id">
                        </div>
                    </div>
                    <div class="form-group">
                    <div class="col-xs-offset-2 col-xs-10">
                        <button type="submit" class="btn btn-primary btn-block">Sign Up</button>
                    </div>
                </div>
                    <div class="form-group">
                        <div class="col-xs-offset-2 col-xs-10">
                            <button class="btn btn-primary">Use Google</button>
                            <button class="btn btn-primary">Use Facebook</button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>




</body>
</html>