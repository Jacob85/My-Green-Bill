package com.mygreenbill.common;

import com.mygreenbill.registration.RegistrationRequestAbstract;
import com.mygreenbill.security.EncryptionType;

import java.util.List;

/**
 * Created by Jacob on 3/29/14.
 */
public class GreenBillUser
{
    private String firstName;
    private String lastName;
    private String userId;
    private String password;
    private String email;
    private List<Integer> userCompanyList;

    public GreenBillUser()
    {
    }

    public GreenBillUser(String firstName, String lastName, String userId, String password, String email, List<Integer> userCompanyList)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.userCompanyList = userCompanyList;
    }

    public GreenBillUser (RegistrationRequestAbstract fullRegistrationRequest)
    {
        this.userId = fullRegistrationRequest.getId();
        this.password = fullRegistrationRequest.getEncryptPassword(EncryptionType.MD5);
        this.email = fullRegistrationRequest.getEmail();
        this.firstName = fullRegistrationRequest.getValidationResponse().getFirstName();
        this.lastName = fullRegistrationRequest.getValidationResponse().getLastName();
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
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

    public boolean isUserObjectFull(boolean needCompanyList)
    {
        return  userId != null &&
                password != null &&
                email != null &&
                (needCompanyList ? userCompanyList != null : true);

    }

}
