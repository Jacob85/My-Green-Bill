package com.mygreenbill.common;

import com.mygreenbill.Exceptions.InitException;
import com.mygreenbill.database.DatabaseHandler;
import com.mygreenbill.security.EncryptionType;
import com.mygreenbill.security.EncryptionUtil;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ipeleg on 5/24/14.
 */
public class SendEmailsHandler
{
    private static SendEmailsHandler sendEmailsHandler;
    private static Logger LOGGER = Logger.getLogger(SendEmailsHandler.class);

    private SendEmailsHandler()
    {

    }

    public static SendEmailsHandler getInstance()
    {
        if (sendEmailsHandler == null)
            sendEmailsHandler = new SendEmailsHandler();
        return sendEmailsHandler;
    }

    private void sendMail(String emailAddress, String emailString, String subject)
    {
        Map<String, String> messageFiled = new HashMap<String, String>();
        messageFiled.put(JsonRequestFields.MESSAGE_TYPE.field(), String.valueOf(MessageType.SEND_MAIL_TO_COSTUMER));
        messageFiled.put(JsonRequestFields.EMAIL_SEND_TO.field(), emailAddress);
        messageFiled.put(JsonRequestFields.EMAIL_SUBJECT.field(), subject);
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

    /**
     * Retrieves the unregister template from DB, construct the email and sends it to the company
     *
     * @param user the user to send to the welcome email
     * @param company the company to send email to
     * @return  Status if the operation was success or not
     */
    public Status sendUnregisterMailToCompany(GreenBillUser user, GreenBillCompany company)
    {
        if (user == null)
        {
            LOGGER.info("User is null, cannot compose validation email");
            return new Status(Status.OperationStatus.FAILED, "User is null, cannot compose unregister email");
        }

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        String emailString = databaseHandler.getEmailTemplate(MailTemplate.UNREGISTER_CUSTOMER);
        if (GeneralUtilities.hasData(emailString))
        {
            // replace the email parameters with the real user data
            emailString = emailString.replace("$company_name", company.getName());
            emailString = emailString.replace("$first_name", user.getFirstName());
            emailString = emailString.replace("$last_name", user.getLastName());
            emailString = emailString.replace("$user_id", user.getUserId());
            emailString = emailString.replace("$user_email", EncryptionUtil.encryptString(user.getEmail(), EncryptionType.MD5) + "@mygreenbill.com");
            LOGGER.debug("replaced the template parameters with real user information: " + emailString);

            //sending message to mail server
            sendMail(company.getEmail(), emailString, String.valueOf(MailTemplate.UNREGISTER_CUSTOMER));
            return new Status(Status.OperationStatus.SUCCESS, "");

        }
        else
        {
            Status status = new Status(Status.OperationStatus.FAILED, "Data Received from DB is empty or null");
            LOGGER.info("Failed to get email template from DB: " + status);
            return status;
        }
    }

    /**
     * Retrieves the register template from DB, construct the email and sends it to the company
     *
     * @param user the user to send to the welcome email
     * @param company the company to send email to
     * @return  Status if the operation was success or not
     */
    public Status sendRegisterMailToCompany(GreenBillUser user, GreenBillCompany company)
    {
        if (user == null)
        {
            LOGGER.info("User is null, cannot compose validation email");
            return new Status(Status.OperationStatus.FAILED, "User is null, cannot compose register email");
        }

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        String emailString = databaseHandler.getEmailTemplate(MailTemplate.REGISTER_CUSTOMER‏);
        if (GeneralUtilities.hasData(emailString))
        {
            // replace the email parameters with the real user data
            emailString = emailString.replace("$company_name", company.getName());
            emailString = emailString.replace("$first_name", user.getFirstName());
            emailString = emailString.replace("$last_name", user.getLastName());
            emailString = emailString.replace("$user_id", user.getUserId());
            emailString = emailString.replace("$user_email", EncryptionUtil.encryptString(user.getEmail(), EncryptionType.MD5) + "@mygreenbill.com");
            LOGGER.debug("replaced the template parameters with real user information: " + emailString);

            //sending message to mail server
            sendMail(company.getEmail(), emailString, String.valueOf(MailTemplate.REGISTER_CUSTOMER‏));
            return new Status(Status.OperationStatus.SUCCESS, "");

        }
        else
        {
            Status status = new Status(Status.OperationStatus.FAILED, "Data Received from DB is empty or null");
            LOGGER.info("Failed to get email template from DB: " + status);
            return status;
        }
    }
}
