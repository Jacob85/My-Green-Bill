package com.mygreenbill.authentication;

import com.mygreenbill.Exceptions.AuthenticationException;
import com.mygreenbill.common.GeneralUtilities;
import com.mygreenbill.common.GreenBillUser;
import com.mygreenbill.common.Status;
import com.mygreenbill.database.DatabaseHandler;
import com.mygreenbill.security.EncryptionType;
import com.mygreenbill.security.EncryptionUtil;
import org.apache.log4j.Logger;
import sun.util.logging.resources.logging;

import javax.servlet.http.HttpSession;

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

        try
        {
            user = databaseHandler.loginUser(email, EncryptionUtil.encryptString(password, EncryptionType.MD5));
            if (user != null)
            {
                LOGGER.info("login process completed, adding sign in record");
                databaseHandler.addSignInRecord(user);
                // update session with the user information
                session.setAttribute("user", user);
                return new Status(Status.OperationStatus.SUCCESS, "Login completed");
            }
        } catch (AuthenticationException e)
        {
            LOGGER.error(String.format("Authentication failed for user %s and password %s message: %s", email, password, e.getMessage()));
            return new Status(Status.OperationStatus.FAILED, e.getMessage());
        }

        //databaseHandler.addSignInRecord()

        return null;
    }

    public Status processLoginRequest(String loginToken) //App login
    {
        return null;
    }
    public Status proccessLogoutRequest(GreenBillUser user)
    {
        return null;
    }
}
