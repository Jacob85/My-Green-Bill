package com.mygreenbill.Exceptions;

/**
 * Created by Jacob on 4/17/14.
 */
public class AuthenticationException extends Exception
{
    public AuthenticationException(String message)
    {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
