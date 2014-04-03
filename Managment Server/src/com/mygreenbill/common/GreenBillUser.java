package com.mygreenbill.common;

import java.util.List;

/**
 * Created by Jacob on 3/29/14.
 */
public class GreenBillUser
{
    private String userId;
    private String password;
    private String email;
    private List<Integer> userCompanyList;

    public GreenBillUser()
    {
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public List<Integer> getUserCompanyList()
    {
        return userCompanyList;
    }

    public void setUserCompanyList(List<Integer> userCompanyList)
    {
        this.userCompanyList = userCompanyList;
    }

}
