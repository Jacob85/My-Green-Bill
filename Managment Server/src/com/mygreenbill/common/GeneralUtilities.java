package com.mygreenbill.common;

import com.mygreenbill.registration.SimpleIdentityValidationResponse;

import java.util.*;

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

    public static boolean hasData(List<?> list)
    {
        if (list == null)
            return false;
        if (list.size() == 0)
            return false;
        return true;
    }

    public static DateRange getLastMonthDateRange()
    {
        Date begining, end;

        {
            Calendar calendar = getCalendarForLastMonth();
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            setTimeToBeginningOfDay(calendar);
            begining = calendar.getTime();
        }

        {
            Calendar calendar = getCalendarForLastMonth();
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndofDay(calendar);
            end = calendar.getTime();
        }

        return new DateRange(begining, end);

    }


    public static DateRange getCurrentMonthDateRange() {
        Date begining, end;

        {
            Calendar calendar = getCalendarForNow();
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            setTimeToBeginningOfDay(calendar);
            begining = calendar.getTime();
        }

        {
            Calendar calendar = getCalendarForNow();
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndofDay(calendar);
            end = calendar.getTime();
        }

        return new DateRange(begining, end);
    }

    private static Calendar getCalendarForNow() {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(new Date());
    return calendar;
    }
    private static Calendar getCalendarForLastMonth() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        return calendar;
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }
}
