package com.mygreenbill.registration;

import com.mygreenbill.Exceptions.InitException;
import com.mygreenbill.Exceptions.UserIdentityException;
import com.mygreenbill.common.ConnectionManager;
import com.mygreenbill.common.GeneralUtilities;
import com.mygreenbill.common.Status;
import com.mygreenbill.database.DatabaseHandler;
import org.apache.log4j.Logger;

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
        //todo yaki - implement the method
        return false;
    }

    @Override
    public Status RegisterUser(RegistrationRequestAbstract request)
    {
        //todo yaki - implement the method
        return null;
    }

    @Override
    public Status updateCurrentSessionWithUserInfo(RegistrationRequestAbstract request)
    {
        //todo yaki - implement the method
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
