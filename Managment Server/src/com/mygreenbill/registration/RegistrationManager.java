package com.mygreenbill.registration;

import com.mygreenbill.Exceptions.InitException;
import com.mygreenbill.Exceptions.UserIdentityException;
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
 * Created by Jacob on 3/17/14.
 */
public class RegistrationManager implements IRegistration
{
    private static RegistrationManager instance = null;
    private static final Logger LOGGER = Logger.getLogger(RegistrationManager.class);


    public static RegistrationManager getInstance()
    {
        if (instance == null)
            instance = new RegistrationManager();
        return instance;
    }

    private RegistrationManager()
    {
        LOGGER.debug(RegistrationManager.class.getSimpleName() + " Was created");
    }


    @Override
    public boolean isUserExists(RegistrationRequestAbstract request)
    {
        //todo yaki - implement the method
        return false;
    }

    @Override
    public boolean validateIdentity(RegistrationRequestAbstract request)
    {
        //todo yaki - implement the method
        return false;
    }

    @Override
    public String getFirstValidationQuestion(RegistrationRequestAbstract request)
    {
        return request.getFirstValidationQuestion().getQuestion();
    }

    @Override
    public String getSecondValidationQuestion(RegistrationRequestAbstract request)
    {
        return request.getSecondValidationQuestion().getQuestion();
    }

    @Override
    public boolean areAnswersValid(RegistrationRequestAbstract request, String answer1, String answer2)
    {
        if (request == null || answer1 == null || answer2 == null)
        {
            LOGGER.info(String.format("Cannot validate the answers (%s, %s) for request (%s)", answer1, answer2, request));
            return false;
        }
        Question question1 = request.getFirstValidationQuestion();
        Question question2 = request.getSecondValidationQuestion();

        if (question1 == null || question2 == null)
        {
            LOGGER.info(String.format("Cannot validate the answers (%s, %s) for questions (%s, %s)", answer1, answer2, question1, question2));
            return false;
        }

        return  question1.getAnswer().equalsIgnoreCase(answer1) && question2.getAnswer().equalsIgnoreCase(answer2);
    }

    @Override
    public Status registerUser(RegistrationRequestAbstract request)
    {
        if (request != null)
        {
            if (request instanceof FullRegistrationRequest)
            {
                FullRegistrationRequest registrationRequest = (FullRegistrationRequest) request;

                // create new user in the database
                LOGGER.info(String.format("Adding the user %s %s to the database", registrationRequest.getValidationResponse().getFirstName(),
                                                                                   registrationRequest.getValidationResponse().getLastName()));
                Status addToDbStatus = DatabaseHandler.getInstance().registerUser(request);
                if (addToDbStatus.getOperationStatus() == Status.OperationStatus.SUCCESS)
                {
                    LOGGER.info("User was added to Database, start to compose Json request and send it to Mil server");
                    // send message to the Mail server to open new account
                    sendRegistrationMessage(registrationRequest);
                    return new Status(Status.OperationStatus.SUCCESS, "user was successfully register");
                }
                else
                {
                    // failed to add user to database
                    return addToDbStatus;
                }
            }
            else
            {
                // registration by app
                //todo yaki - implement same procedure for registration by app
            }

        }
        return null;
    }

    private void sendRegistrationMessage(FullRegistrationRequest registrationRequest)
    {
        Map<String, String> messageFiled = new HashMap<String, String>();
        messageFiled.put("MessageType", String.valueOf(MessageType.ADD_USER));
        messageFiled.put("userId", registrationRequest.getId());
        messageFiled.put("accountName", EncryptionUtil.encryptString(registrationRequest.getEmail(), EncryptionType.MD5));
        messageFiled.put("password", registrationRequest.getEncryptPassword(EncryptionType.MD5));
        messageFiled.put("address", registrationRequest.getEmail());
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

    /**
     * Create new Green bill user and add it to the current Session
     * @param request the original registration request
     * @param currentSession The current user Session
     * @return {@link com.mygreenbill.common.Status} object representing the operation status
     */
    @Override
    public Status updateCurrentSessionWithUserInfo(RegistrationRequestAbstract request, HttpSession currentSession)
    {
        if (request == null || currentSession == null)
        {
            LOGGER.info("Failed to update Current Session With User Info the request or the session is null");
            return new Status(Status.OperationStatus.FAILED, "Failed to update Current Session With User Info the request or the session is null");
        }
        if (request instanceof FullRegistrationRequest)
        {
            GreenBillUser registeredUser = new GreenBillUser((FullRegistrationRequest) request);
            currentSession.setAttribute("user", registeredUser);
            return  new Status(Status.OperationStatus.SUCCESS, "Current Session Was Updated With User Info ");
        }

        return null;
    }

    @Override
    public Status processRegistrationRequest(RegistrationRequestAbstract registrationRequest)
    {
        // check that the request is valid
        if (!registrationRequest.isRequestValid())
        {
            LOGGER.info("Unable to process registration request, invalid request: " + registrationRequest);
            return new Status(Status.OperationStatus.FAILED, "Unable to process registration request, invalid request: " + registrationRequest);
        }

        // check in the DB that the user does not exists
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
        boolean exists = databaseHandler.isUserExist(registrationRequest.getId(), false);
        if(exists)
        {
            LOGGER.info("User already exists!");
            return new Status(Status.OperationStatus.FAILED, "User already exists");
        }

        try
        {
            ConnectionManager connectionManager = ConnectionManager.getInstance();
            SimpleIdentityValidationResponse response = connectionManager.getUserIdentity(registrationRequest.getId());
            //check if the response is valid
            if (GeneralUtilities.isValidationResponseValid(response))
            {
                LOGGER.info("Updating validation response into the registration request: ");
                LOGGER.info("Fetching validation succeeded!");
                //update the SimpleIdentityValidationResponse in the registration request object
                registrationRequest.setValidationResponse(response);
                return new Status(Status.OperationStatus.SUCCESS, "Success");
            }

        } catch (InitException e)
        {
            LOGGER.info("Unable to init the connection manager!");
            return new Status(Status.OperationStatus.FAILED, "Unable to init the connection manager");
        } catch (UserIdentityException e)
        {
            LOGGER.info("Problem with user identity!");
            return new Status(Status.OperationStatus.FAILED, "Problem with user identity");
        }
        return new Status(Status.OperationStatus.FAILED, "Validation failed");
    }
}
