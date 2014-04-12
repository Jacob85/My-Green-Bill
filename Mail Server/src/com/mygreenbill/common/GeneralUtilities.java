package com.mygreenbill.common;

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
}
