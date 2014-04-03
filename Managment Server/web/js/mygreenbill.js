/**
 * Created by Jacob on 3/15/14.
 */


function fullRegistration()
{

    console.log("Full registration pressed");
    $('#full_registration').modal('show');
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
            },
          success: function(element) {
                element
                    .text('OK!').addClass('valid')
                    .closest('.form-group').removeClass('error').addClass('success');
            }
        });

   // jQuery.noConflict();
}); // end document.ready

function openPopUp()
{
    jQuery.noConflict();
    $('#sign_up_model').modal('show');
}