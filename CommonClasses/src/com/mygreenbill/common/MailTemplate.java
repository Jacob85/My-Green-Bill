package com.mygreenbill.common;

/**
 * Created by Jacob on 5/3/14.
 */
public enum MailTemplate
{
    WELCOME("Welcome"),
    PASSWORD_RESET("Password Reset"),
    MONTHLY_UPDATE("Monthly update");

    private String dataBaseName;

    MailTemplate(String dataBaseName)
    {
        this.dataBaseName = dataBaseName;
    }

    public String getDataBaseName()
    {
        return dataBaseName;
    }
}
