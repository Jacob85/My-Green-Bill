package com.mygreenbill.Exceptions;

/**
 * Created by Jacob on 3/29/14.
 */
public class UserIdentityException extends Exception
{
    public UserIdentityException(String message)
    {
        super(message);
    }

    public UserIdentityException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
