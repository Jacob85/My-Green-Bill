package com.mygreenbill.common;

import java.util.Date;

/**
 * Created by Jacob on 5/24/14.
 */
public class DateRange
{
    private Date startDate;
    private Date endDate;

    public DateRange(Date startDate, Date endDate)
    {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DateRange()
    {
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }
}
