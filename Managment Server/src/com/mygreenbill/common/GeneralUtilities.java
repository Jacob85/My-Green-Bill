package com.mygreenbill.common;

import com.mygreenbill.registration.SimpleIdentityValidationResponse;

/**
 * Created by Jacob on 3/29/14.
 */
public class GeneralUtilities
{
    private GeneralUtilities(){}


    /**
     * checks if the id is valid:
     * <ul>
     *     <li>id is not null</li>
     *     <li>id is not empty</li>
     *     <li>id contains only numbers</li>
     *     <li>id length >= 8</li>
     * </ul>
     * @param id the id to check
     * @return
     */
    public static boolean isIdValid(String id)
    {
        if (id == null)
            return false;
        if (id.isEmpty())
            return false;
        //test that the string contains only numbers and length >=8
        if (!id.matches("[0-9]+") || id.length() < 8)
            return false;
        return true;
    }

    /**
     * If the "is_alive" field set to false return false
     * If the user is dead return false
     * @param response the response from the ministry of interior
     * @return true if valid else false
     */
    public static boolean isValidationResponseValid(SimpleIdentityValidationResponse response)
    {
        return (response != null && response.isAlive());
    }

    /**
     * check if a string contains information in it and not null
     * @param string the string to check
     * @return false if null or if empty, true otherwise
     */
    public static boolean hasData(String string)
    {
        if (string == null)
            return false;
        if (string.equals(""))
            return false;
        if (string.isEmpty())
            return false;
        return true;
    }
}
