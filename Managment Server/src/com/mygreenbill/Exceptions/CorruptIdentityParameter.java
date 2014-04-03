package com.mygreenbill.Exceptions;

/**
 * Created by Jacob on 3/17/14.
 */
public class CorruptIdentityParameter extends Exception
{
    public CorruptIdentityParameter(String message)
    {
        super(message);
    }

    public CorruptIdentityParameter(String message, Throwable cause)
    {
        super(message, cause);
    }
}
