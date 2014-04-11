package com.mygreenbill.authentication;

/**
 * Created by Jacob on 4/8/14.
 */
public class AuthenticationManager
{
    private static AuthenticationManager instance;

    public static AuthenticationManager getInstance()
    {
        if (instance == null)
            instance = new AuthenticationManager();
        return instance;
    }

    private AuthenticationManager(){}


}
