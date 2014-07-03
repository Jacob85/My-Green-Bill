/**
 * Created by Jacob on 3/15/14.
 */


function fullRegistration()
{

    console.log("Full registration pressed");
    $('#full_registration').modal('show');
}


function validateLoginForm ()
{
    var email = $('#login_form_email').value;
    var password = $('#login_form_password').value;

    if (email == null || email == "")
    {
        alert("Please enter email address");
        return false;
    }
    if (password == null || password =="" || password.length < 6)
    {
        alert("Please enter a valid password (6 characters length)");
        return false;
    }
    return true;
}
function validateFullRegistrationForm()
{
    var email = $('#full_registration_inputEmail').val();
    var password = $('#full_registration_inputPassword').val();
    var confirmedPassword = $('#full_registration_confirmPassword').val();
    var id = $('#full_registration_inputId').val();

    if (!isEmailAddressValid(email))
    {

    }




    console.log(email + password + confirmedPassword + id);
}

function isNumber(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}

function isEmailAddressValid(email)
{
    var atpos=email.indexOf("@");
    var dotpos=email.lastIndexOf(".");
    if (atpos<1 || dotpos<atpos+2 || dotpos+2>=email.length)
    {
        return false;
    }
    return true;
}

$(document).ready(function(){
    $('#registration_modal').on('shown.bs.modal', function() {
        $('#toogleSignUp').click();
    })
});
var firstTime = true;
$(document).ready(function(){

    $('#form-signin').validate(
        {
            rules:{
                login_form_email:{
                    required: true,
                    email: true
                },
                login_form_password:{
                    required: true,
                    password: true,
                    minlength: 6
                }
            },
            highlight: function(element) {
                $(element).closest('.form-group').removeClass('success').addClass('error');
            }
        }
    );


    $('#appLoginForm').validate(
        {
            rules:
            {
                appRegisterId:
                {
                    required: true,
                    minlength: 8,
                    number: true
                }
            },
            highlight: function(element) {
                $(element).closest('.form-group').removeClass('success').addClass('error');
            },
            submitHandler: function(form){
                console.log("submit Called");

                if (firstTime)
                {
                    firstTime=false;
                    googleLogin(signinCallback);
                    event.preventDefault();
                }
                else
                {
                    form.submit();
                }
            }
        });

    $('#passwordRestoreForm').validate(
        {
            rules:
            {
                password: {
                    required: true,
                    minlength: 6
                },
                reenteredPassword: {
                    required: true,
                    minlength: 6,
                    equalTo: "#password"
                }
            },
            highlight: function(element) {
                $(element).closest('.form-group').removeClass('success').addClass('error');
            }
        }
    );
    $('#full_registration_form').validate(
        {
            rules: {
                full_registration_inputEmail: {
                    required: true,
                    email: true
                },
                full_registration_inputPassword: {
                    required: true,
                    minlength: 6
                },
                full_registration_confirmPassword: {
                    required: true,
                    minlength: 6,
                    equalTo: "#full_registration_inputPassword"
                },
                full_registration_inputId: {
                    required: true,
                    minlength: 8,
                    number: true
                }
            },
            highlight: function(element) {
                $(element).closest('.form-group').removeClass('success').addClass('error');
            }/*,
          success: function(element) {
                element
                    .closest('.form-group').removeClass('error').addClass('success');
            }*/
        });



    $('#loginWithGoogle').click(function(){
        googleLogin(signInOnlyCallback);
    });
}); // end document.ready


function googleLogin(callback)
{
    var myParams = {
        'clientid' : '288366509317-1hgkph86vukk0iu4983vsol50b24l0bg.apps.googleusercontent.com', //You need to set client id
        'cookiepolicy' : 'single_host_origin',
        'callback' : callback, //callback function
//        'approvalprompt':'force',
        'scope' : 'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email'
    };
    gapi.auth.signIn(myParams);
}
function signInOnlyCallback(authResult)
{
    if (authResult['status']['signed_in']) {
        // Update the app to reflect a signed in user
        // Hide the sign-in button now that the user is authorized, for example:
        //document.getElementById('signinButton').setAttribute('style', 'display: none');
        console.log("Success!!");
        $.getJSON("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + authResult["access_token"], function (data){
            console.log(data);
            console.log(data.email);
            addFormHiddenParamsAndSubmit(data, '#goggleLoginForm');

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
}
function signinCallback(authResult)
{
    if (authResult['status']['signed_in']) {
        // Update the app to reflect a signed in user
        // Hide the sign-in button now that the user is authorized, for example:
        //document.getElementById('signinButton').setAttribute('style', 'display: none');
        console.log("Success!!");
        $.getJSON("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + authResult["access_token"], function (data){
            console.log(data);
            console.log(data.email);
            addFormHiddenParamsAndSubmit(data, "#appLoginForm");

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
}

function addFormHiddenParamsAndSubmit(data, formID){

    $('<input />').attr('type', 'hidden')
        .attr('name', 'firstName')
        .attr('value', data.given_name)
        .appendTo(formID);

    $('<input />').attr('type', 'hidden')
        .attr('name', 'lastName')
        .attr('value', data.family_name)
        .appendTo(formID);

    $('<input />').attr('type', 'hidden')
        .attr('name', 'email')
        .attr('value', data.email)
        .appendTo(formID);

    $('<input />').attr('type', 'hidden')
        .attr('name', 'picture')
        .attr('value', data.picture)
        .appendTo(formID);

     /*submit the form*/
    $( formID).submit();
}


$(document).ready(function() {
    $('.popup-youtube').magnificPopup({
        disableOn: 850,
        type: 'iframe',
        mainClass: 'mfp-fade',
        removalDelay: 160,
        preloader: false,

        fixedContentPos: false
    });
});
