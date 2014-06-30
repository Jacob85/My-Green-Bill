package com.mygreenbill.authentication;

import com.mygreenbill.Exceptions.DatabaseException;
import com.mygreenbill.Exceptions.InitException;
import com.mygreenbill.common.*;
import com.mygreenbill.database.DatabaseHandler;
import com.mygreenbill.registration.AppRegistrationRequest;
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

    public Status processRestorePasswordRequest(String email, HttpSession session)
    {
        if (!GeneralUtilities.hasData(email))
        {
            LOGGER.info("invalid email received while trying to restore password: " + email);
            return new Status(Status.OperationStatus.FAILED,"invalid email received while trying to restore password: " + email);
        }

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        GreenBillUser greenBillUser = databaseHandler.retrieveUserInformation(email);
        if (greenBillUser != null)
        {
            LOGGER.debug("user information is: " + greenBillUser + " continue to restore password");
            // check if the user password is "Default Password"
            if (greenBillUser.getPassword().equals(EncryptionUtil.encryptString(AppRegistrationRequest.defaultPassword, EncryptionType.MD5)))
            {
                //user was register by app, cannot restore password
                LOGGER.info("Unable to restore password for user: " + greenBillUser + " user was register by app");
                return new Status(Status.OperationStatus.FAILED, "Unable to restore password for user: " + greenBillUser.getFullName() + " user was register by app" );
            }
            else
            {
                //user was fully register and need to restore its password
                session.setAttribute("user", greenBillUser);
                return composeAndSendEmailFromTemplate(greenBillUser, MailTemplate.PASSWORD_RESET);
            }
        }
        else
        {
            LOGGER.info("Failed to retrieve user information for email: " + email);
            return new Status(Status.OperationStatus.FAILED, "Failed to retrieve user information for email: " + email);
        }

    }

    /**
     * Retrieves the mail template from DB, construct custom welcome email (including activation url) to the user and send it to the mail server in order to send it to the user.
     *
     * @param user the user to send to the welcome email
     * @return  Status if the operation was success or not
     */
    public Status composeAndSendEmailFromTemplate(GreenBillUser user, MailTemplate mailTemplate)
    {
        if (user == null)
        {
            LOGGER.info("User is null, cannot compose  email");
            return new Status(Status.OperationStatus.FAILED, "User is null, cannot compose email");
        }

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        String emailString = databaseHandler.getEmailTemplate(mailTemplate);
        if (GeneralUtilities.hasData(emailString))
        {
            // replace the email parameters with the real user data
            emailString = emailString.replace("$first_name", user.getFirstName());
            emailString = emailString.replace("$last_name", user.getLastName());
            emailString = emailString.replace("$link", composeUrl(user, mailTemplate));
            LOGGER.debug("replaced the template parameters with real user information: " + emailString);

            //sending message to mail server
            sendMailToCustomer(user, emailString, mailTemplate);
            return new Status(Status.OperationStatus.SUCCESS, "");

        }
        else
        {
            Status status = new Status(Status.OperationStatus.FAILED, "Data Received from DB is empty or null");
            LOGGER.info("Failed to get email template from DB: " + status);
            return status;
        }
    }

    private void sendMailToCustomer(GreenBillUser user, String emailString, MailTemplate mailTemplate)
    {
        Map<String, String> messageFiled = new HashMap<String, String>();
        messageFiled.put(JsonRequestFields.MESSAGE_TYPE.field(), String.valueOf(MessageType.SEND_MAIL_TO_COSTUMER));
        messageFiled.put(JsonRequestFields.EMAIL_SEND_TO.field(), user.getForwardEmail()); // Sending the email to the forward address of the user
        messageFiled.put(JsonRequestFields.EMAIL_SUBJECT.field(), mailTemplate.getDataBaseName());
        messageFiled.put(JsonRequestFields.MESSAGE_CONTENT.field(), emailString);
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

    private String composeUrl(GreenBillUser user, MailTemplate mailTemplate)
    {
        if (user == null)
            return null;
        if (mailTemplate == MailTemplate.WELCOME)
        {
            // The hush validation is 2 times MD5 on the user email.
            return "http://mygreenbill.com/greenbill/authenticate/accountActivation?email=" +user.getEmail() +
                    "&hash=" + EncryptionUtil.encryptString(EncryptionUtil.encryptString(user.getEmail(), EncryptionType.MD5), EncryptionType.MD5);
        }
        else
        {
            // compose restore password url!
            return "http://mygreenbill.com/greenbill/authenticate/restorePasswordUrl?email=" +user.getEmail() +
                    "&hash=" + EncryptionUtil.encryptString(EncryptionUtil.encryptString(user.getEmail(), EncryptionType.MD5), EncryptionType.MD5);
        }
    }



    private void sendActivationMessage(GreenBillUser greenBillUser)
    {
        Map<String, String> messageFiled = new HashMap<String, String>();
        messageFiled.put(JsonRequestFields.MESSAGE_TYPE.field(), String.valueOf(MessageType.ADD_USER));
        messageFiled.put(JsonRequestFields.ACCOUNT_NAME.field(), EncryptionUtil.encryptString(greenBillUser.getEmail(), EncryptionType.MD5));
        messageFiled.put(JsonRequestFields.PASSWORD.field(), greenBillUser.getPassword());
        messageFiled.put(JsonRequestFields.EMAIL_ADDRESS.field(), greenBillUser.getForwardEmail()); // Sending the email to the forward address of the user
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

    public Status setNewForwardAddress(GreenBillUser greenBillUser, String newAddress)
    {
        Status status = null;
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

        try
        {
            status = databaseHandler.runUpdateQuery(String.format("CALL SetNewForwardAddress('%s','%s');", greenBillUser.getEmail(), newAddress));

            if (status.getOperationStatus() == Status.OperationStatus.SUCCESS)
            {
                Map<String, String> messageFiled = new HashMap<String, String>();
                messageFiled.put(JsonRequestFields.MESSAGE_TYPE.field(), String.valueOf(MessageType.SET_NEW_FORWARD_ADDRESS));
                messageFiled.put(JsonRequestFields.ACCOUNT_NAME.field(), EncryptionUtil.encryptString(greenBillUser.getEmail(), EncryptionType.MD5));
                messageFiled.put(JsonRequestFields.FORWARD_ADDRESS.field(), newAddress);
                JSONObject message = new JSONObject(messageFiled);
                LOGGER.info("Finished to construct json request : " + message.toString());
                LOGGER.info("Sending message to mail server");

                ConnectionManager.getInstance().sendToTrafficBlade(new JSONObject(messageFiled));
            }
        }
        catch (DatabaseException e)
        {
            LOGGER.error("DatabaseException in setNewForwardAddress");
            LOGGER.error(e.getMessage(), e);
            status = new Status(Status.OperationStatus.FAILED, "DatabaseException in setNewForwardAddress");
        }
        catch (InitException e)
        {
            LOGGER.error("InitException in setNewForwardAddress");
            LOGGER.error(e.getMessage(), e);
            status = new Status(Status.OperationStatus.FAILED, "InitException in setNewForwardAddress");
        }

        return status;
    }

    public Status processRestorePasswordRequestPressed(String email, String hash, HttpSession session)
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
            returnStatus.setOperationStatus(Status.OperationStatus.SUCCESS);
        }
        else
        {
            returnStatus.setOperationStatus(Status.OperationStatus.FAILED);
            returnStatus.setDescription("wrong link pressed...");

        }
        LOGGER.info(returnStatus);
        return returnStatus;
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
            LOGGER.info("Current session was update with user details fro user name: " + greenBillUser.getFirstName());

            returnStatus.setOperationStatus(Status.OperationStatus.SUCCESS);
            returnStatus.setDescription("User Authentication was success!");
            return returnStatus;
        }
        return  returnStatus;
    }

    public Status changePassword(GreenBillUser greenBillUser, String newPassword)
    {
        if (!GeneralUtilities.hasData(newPassword) || greenBillUser == null)
        {
            LOGGER.info("Failed to update new password, invalid parameter received: " + greenBillUser + " pass:" + newPassword);
            return new Status(Status.OperationStatus.FAILED, "Failed to update new password, invalid parameter received");
        }
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        Status status = databaseHandler.changeUserPassword(greenBillUser, EncryptionUtil.encryptString(newPassword, EncryptionType.MD5));
        if (status.getOperationStatus() == Status.OperationStatus.SUCCESS)
            sendRestorePasswordMessage(greenBillUser, newPassword);
        return status;
    }

    private void sendRestorePasswordMessage(GreenBillUser greenBillUser, String newPassword)
    {
        String mailTemplate = DatabaseHandler.getInstance().getEmailTemplate(MailTemplate.PASSWORD_RESET_SUCCESS);
        if (GeneralUtilities.hasData(mailTemplate))
        {
            mailTemplate = mailTemplate.replace("$first_name", greenBillUser.getFirstName());
            mailTemplate = mailTemplate.replace("$last_name", greenBillUser.getLastName());
            mailTemplate = mailTemplate.replace("$pass", newPassword);
        }
        LOGGER.debug("replaced the template parameters with real user information: " + mailTemplate);

        //sending message to mail server
        sendMailToCustomer(greenBillUser, mailTemplate, MailTemplate.PASSWORD_RESET_SUCCESS);
    }

    public Status processContactUs(String email, String name, String title, String content)
    {
        if (!GeneralUtilities.hasData(email) || !GeneralUtilities.hasData(content))
        {
            LOGGER.info("cannot process contact inquiry, the email or the message inserted are empty ");
            return new Status(Status.OperationStatus.FAILED, "cannot process contact inquiry, the email or the message inserted are empty");
        }

        /*Generate the query string*/
        String query = "insert into mygreenbilldb.users_inquiry (email, name, title, content)  values ('$email', '$name', '$title', '$content');";
        query = query.replace("$email", email);
        query = query.replace("$name", name);
        query = query.replace("$title", title);
        query = query.replace("$content", content);
        LOGGER.debug("Query String to add inquiry was generated: " + query);

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        try
        {
            LOGGER.info("run the insert query..");
            Status status = databaseHandler.runInsertQuery(query);
            LOGGER.debug(status);
            return status;
        } catch (DatabaseException e)
        {
            LOGGER.error(e.getMessage(), e);
            return new Status(Status.OperationStatus.FAILED, "Something went wrong in DB.. " + e.getMessage());
        }
    }
}
