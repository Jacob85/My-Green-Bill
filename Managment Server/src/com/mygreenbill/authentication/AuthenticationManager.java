package com.mygreenbill.authentication;

import com.mygreenbill.Exceptions.InitException;
import com.mygreenbill.common.*;
import com.mygreenbill.database.DatabaseHandler;
import com.mygreenbill.security.EncryptionType;
import com.mygreenbill.security.EncryptionUtil;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jacob on 4/8/14.
 */
public class AuthenticationManager
{
    private static AuthenticationManager instance;
    private static Logger LOGGER = Logger.getLogger(AuthenticationManager.class);
    public static AuthenticationManager getInstance()
    {
        if (instance == null)
            instance = new AuthenticationManager();
        return instance;
    }

    private AuthenticationManager(){}

    /**
     * this method check if the user login request is valid, means if the user exists, and the password inserted is correct
     * Then retrieve the user information from the database, create new {@link com.mygreenbill.common.GreenBillUser} from tis information
     * and attache the {@link com.mygreenbill.common.GreenBillUser} to the current session
     * @param email The user email address
     * @param password The user password
     * @param session The current session to update the user into
     * @return {@link com.mygreenbill.common.Status} Object which represents the operation status
     */
    public Status processLoginRequest(String email, String password, HttpSession session)
    {
        if (!GeneralUtilities.hasData(email) || !GeneralUtilities.hasData(password))
        {
            LOGGER.info(String.format("Cannot process login request for email %s and password %s", email, password));
            return new Status(Status.OperationStatus.FAILED, String.format("Cannot process login request for email %s and password %s", email, password));
        }

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        GreenBillUser user;

        //try to login the user;
        LoginStatus loginStatus = databaseHandler.loginUser(email, EncryptionUtil.encryptString(password, EncryptionType.MD5));
        if (loginStatus.getLoginOperationStatus() == LoginStatus.LoginOperationStatus.SUCCESS)
        {
            user = loginStatus.getGreenBillUser();
            LOGGER.info("login process completed, adding sign in record");
            databaseHandler.addSignInRecord(user);
            // update session with the user information
            session.setAttribute("user", user);
            return new Status(Status.OperationStatus.SUCCESS, "Login completed");
        }
        else
        {
            // login failed make sure to display the right message to the user from the LoginStatus.LoginOperationStatus
            LOGGER.info("User login failed do to: " + loginStatus.getLoginOperationStatus().getDescription());
            return new Status(Status.OperationStatus.FAILED, loginStatus.getLoginOperationStatus().getDescription());
        }
    }

    public Status processLoginRequest(String loginToken) //App login
    {
        return null;
    }
    public Status proccessLogoutRequest(GreenBillUser user)
    {
        return null;
    }

    /**
     * Retrieves the welcome template from DB, construct custom welcome email (including activation url) to the user and send it to the mail server in order to send it to the user.
     *
     * @param user the user to send to the welcome email
     * @return  Status if the operation was success or not
     */
    public Status composeAndSendAuthenticationEmail(GreenBillUser user)
    {
        if (user == null)
        {
            LOGGER.info("User is null, cannot compose validation email");
            return new Status(Status.OperationStatus.FAILED, "User is null, cannot compose validation email");
        }

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        String emailString = databaseHandler.getEmailTemplate(MailTemplate.WELCOME);
        if (GeneralUtilities.hasData(emailString))
        {
            // replace the email parameters with the real user data
            emailString = emailString.replace("$first_name", user.getFirstName());
            emailString = emailString.replace("$last_name", user.getLastName());
            emailString = emailString.replace("$link", composeAuthenticationUrl(user));
            LOGGER.debug("replaced the template parameters with real user information: " + emailString);

            //sending message to mail server
            sendMailToCustomer(user, emailString);
            return new Status(Status.OperationStatus.SUCCESS, "");

        }
        else
        {
            Status status = new Status(Status.OperationStatus.FAILED, "Data Received from DB is empty or null");
            LOGGER.info("Failed to get email template from DB: " + status);
            return status;
        }
    }

    private void sendMailToCustomer(GreenBillUser user, String emailString)
    {
        Map<String, String> messageFiled = new HashMap<String, String>();
        messageFiled.put("MessageType", String.valueOf(MessageType.SEND_MAIL_TO_COSTUMER));
        messageFiled.put("To", user.getEmail());
        messageFiled.put("Subject", "Welcome");
        messageFiled.put("MessageContent", emailString);
        JSONObject message = new JSONObject(messageFiled);
        LOGGER.info(String.format("Finished to create Json request from type %s, sending message to mail server", MessageType.SEND_MAIL_TO_COSTUMER));

        try
        {
            ConnectionManager.getInstance().sendToTrafficBlade(message);
        } catch (InitException e)
        {
            LOGGER.error("Failed to send json request to mail server", e);
        }
    }

    private String composeAuthenticationUrl(GreenBillUser user)
    {
        if (user == null)
            return null;

        // The hush validation is 2 times MD5 on the user email.
        return "http://mygreenbill.com/greenbill/authenticate/accountActivation?email=" +user.getEmail() +
                "&hash=" + EncryptionUtil.encryptString(EncryptionUtil.encryptString(user.getEmail(), EncryptionType.MD5), EncryptionType.MD5);
    }

    public Status processActivationRequest(String email, String hash, HttpSession session)
    {
        Status returnStatus = new Status();
        returnStatus.setOperationStatus(Status.OperationStatus.FAILED);
        if (!GeneralUtilities.hasData(email) && !GeneralUtilities.hasData(hash))
        {
            returnStatus.setOperationStatus(Status.OperationStatus.FAILED);
            returnStatus.setDescription("Cannot process activation request, parameters are: " + email + " " + hash);
            LOGGER.info(returnStatus.getDescription());
            return returnStatus;
        }

        String hashedEmail = EncryptionUtil.encryptString(EncryptionUtil.encryptString(email, EncryptionType.MD5), EncryptionType.MD5);
        if (hashedEmail.equals(hash))
        {
            //Activation url is valid
            LOGGER.info(String.format("Activation hash %s is valid! activating user for email %s", hash, email));
            LOGGER.debug("Sending Json request from type " + MessageType.ADD_USER + " To Mail server");

            DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
            GreenBillUser greenBillUser = databaseHandler.retrieveUserInformation(email);
            sendActivationMessage(greenBillUser);
            LOGGER.debug("User was successfully activate his account, json request was sent to Mail server");

            session.setAttribute("user", greenBillUser);
            LOGGER.info("Current session was update with user details fro user name: " + greenBillUser.getFirstName() + " " + greenBillUser.getLastName());

            returnStatus.setOperationStatus(Status.OperationStatus.SUCCESS);
            returnStatus.setDescription("User Authentication was success!");
            return returnStatus;
        }
        return  returnStatus;
    }

    private void sendActivationMessage(GreenBillUser greenBillUser)
    {
        Map<String, String> messageFiled = new HashMap<String, String>();
        messageFiled.put("MessageType", String.valueOf(MessageType.ADD_USER));
        messageFiled.put("accountName", EncryptionUtil.encryptString(greenBillUser.getEmail(), EncryptionType.MD5));
        messageFiled.put("password", greenBillUser.getPassword());
        messageFiled.put("address", greenBillUser.getEmail());
        JSONObject message = new JSONObject(messageFiled);
        LOGGER.info("Finished to construct json request : " + message.toString());
        LOGGER.info("Sending message to mail server");
        try
        {
            ConnectionManager.getInstance().sendToTrafficBlade(new JSONObject(messageFiled));

        } catch (InitException e)
        {
            LOGGER.info("Failed to send message to mail server");
            LOGGER.error(e);
        }
    }
}
