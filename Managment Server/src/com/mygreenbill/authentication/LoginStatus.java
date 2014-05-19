package com.mygreenbill.authentication;

import com.mygreenbill.common.GreenBillUser;
import com.mygreenbill.common.Status;

/**
 * This class describes a login status to the system, if the login was success {@link com.mygreenbill.common.GreenBillUser} will be
 * retried from the data base.
 *
 * if the password inserted to login was incorrect the {@link com.mygreenbill.common.GreenBillUser} will be null;
 *
 * The class contains {@link com.mygreenbill.authentication.LoginStatus.LoginOperationStatus} which describes the login status;
 * Created by Jacob on 5/3/14.
 */
public class LoginStatus extends Status
{
    GreenBillUser greenBillUser;
    LoginOperationStatus loginOperationStatus;

    public LoginStatus(OperationStatus operationStatus, String description, GreenBillUser greenBillUser, LoginOperationStatus loginOperationStatus)
    {
        super(operationStatus, description);
        this.greenBillUser = greenBillUser;
        this.loginOperationStatus = loginOperationStatus;
    }

    public LoginStatus(GreenBillUser greenBillUser, LoginOperationStatus loginOperationStatus)
    {
        this.greenBillUser = greenBillUser;
        this.loginOperationStatus = loginOperationStatus;
    }

    public GreenBillUser getGreenBillUser()
    {
        return greenBillUser;
    }

    public void setGreenBillUser(GreenBillUser greenBillUser)
    {
        this.greenBillUser = greenBillUser;
    }

    public LoginOperationStatus getLoginOperationStatus()
    {
        return loginOperationStatus;
    }

    public void setLoginOperationStatus(LoginOperationStatus loginOperationStatus)
    {
        this.loginOperationStatus = loginOperationStatus;
    }

    public enum LoginOperationStatus
    {
        SUCCESS("Success"),
        FAILED_WRONG_PASSWORD("Failed to login, Wrong Password"),
        FAILED_USER_IS_NOT_ACTIVE("Failed to login, User is not active"),
        USER_DOES_NOT_EXISTS("User does not exists");

        private String description;
        LoginOperationStatus(String description)
        {
            this.description = description;
        }

        public String getDescription()
        {
            return description;
        }
    }
}
