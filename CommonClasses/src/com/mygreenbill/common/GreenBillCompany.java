package com.mygreenbill.common;

import java.util.Map;

/**
 * Created by Jacob on 5/15/14.
 */
public class GreenBillCompany
{
    private int id;
    private String email;
    private String name;

    private final String companyIdDatabaseKey = "company_id";
    private final String companyEmailDatabaseKey = "company_email";
    private final String companyNameDatabaseKey = "company_name";

    public GreenBillCompany(int id, String email, String name)
    {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public GreenBillCompany()
    {
    }

    public GreenBillCompany(Map currMap)
    {
        this.id = Integer.parseInt(String.valueOf(currMap.get(companyIdDatabaseKey)));
        this.email = String.valueOf(currMap.get(companyEmailDatabaseKey));
        this.name = String.valueOf(currMap.get(companyNameDatabaseKey));
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
