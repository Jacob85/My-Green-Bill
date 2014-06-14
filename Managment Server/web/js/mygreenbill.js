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

var firstTime = true;
$(document).ready(function(){

    $('#appLoginForm').validate(
        {
            rules:
            {
                appRegisterId:
                {
                    required: function()
                    {
                        var selected = $("#appLoginForm input[type='radio']:checked");
                        var selectedId = selected.attr('id');
                        console.log("Selected Radio button id is: " + selectedId);
                        if (selectedId == "registerRadioBox")
                        {
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    },
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
                    googleLogin();
                    event.preventDefault();
                }
                else
                {
                    var origAction = $('#appLoginForm').attr('action');
                    var selected = $("#appLoginForm input[type='radio']:checked");
                    /*change the form action*/
                    console.log("changing form action from" + origAction + " to " + origAction + selected.val());
                    $('#appLoginForm').attr('action', origAction + selected.val());
                    form.submit();
                }
            }
        });


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

   // jQuery.noConflict();

    // set the content of the front page
    $('#marketing_text').text("Tired of losing your monthly bills? " +
        "The bills went lost by the post office again? " +
        "Finding it hard to keep track on your household expenses? " +
        "If the answer is yes, let us help you... check out this video...");


}); // end document.ready

function openPopUp()
{
    jQuery.noConflict();
    $('#sign_up_model').modal('show');
}

function googleLogin()
{
    var myParams = {
        'clientid' : '288366509317-1hgkph86vukk0iu4983vsol50b24l0bg.apps.googleusercontent.com', //You need to set client id
        'cookiepolicy' : 'single_host_origin',
        'callback' : 'signinCallback', //callback function
//        'approvalprompt':'force',
        'scope' : 'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email'
    };
    gapi.auth.signIn(myParams);
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
            addFormHiddenParamsAndSubmit(data);

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

function addFormHiddenParamsAndSubmit(data){

    $('<input />').attr('type', 'hidden')
        .attr('name', 'firstName')
        .attr('value', data.given_name)
        .appendTo('#appLoginForm');

    $('<input />').attr('type', 'hidden')
        .attr('name', 'lastName')
        .attr('value', data.family_name)
        .appendTo('#appLoginForm');

    $('<input />').attr('type', 'hidden')
        .attr('name', 'email')
        .attr('value', data.email)
        .appendTo('#appLoginForm');

    $('<input />').attr('type', 'hidden')
        .attr('name', 'picture')
        .attr('value', data.picture)
        .appendTo('#appLoginForm');

     /*submit the form*/
    $('#appLoginForm').submit();
}
