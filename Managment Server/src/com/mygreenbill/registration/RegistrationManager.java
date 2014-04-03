package com.mygreenbill.registration;

import com.mygreenbill.common.Status;
import org.apache.log4j.Logger;

/**
 * Created by Jacob on 3/17/14.
 */
public class RegistrationManager implements IRegistration
{
    private static RegistrationManager instance = new RegistrationManager();
    private static final Logger LOGGER = Logger.getLogger(RegistrationManager.class);


    public static RegistrationManager getInstance()
    {
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
        //todo yaki - implement the method
        return null;
    }

    @Override
    public String getSecondValidationQuestion(RegistrationRequestAbstract request)
    {
        //todo yaki - implement the method
        return null;
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
}
