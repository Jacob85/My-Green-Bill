package com.mygreenbill.common;

import com.mygreenbill.registration.RegistrationRequestAbstract;
import com.mygreenbill.security.EncryptionType;
import org.omg.PortableInterceptor.ServerRequestInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Jacob on 3/29/14.
 */
public class GreenBillUser
{
    // private members
    private String firstName;
    private String lastName;
    private String userId;
    private String password;
    private String email;
    private List<Integer> userCompanyList;

    //database filed
    private final String emailKey = "email";
    private final String firstNameKey = "first_name";
    private final String idKey = "id";
    private final String lastNameKey = "last_name";
    private final String passwordKey = "password";

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

    public GreenBillUser(Map map)
    {
        this.email = (String) map.get(emailKey);
        this.password = (String) map.get(passwordKey);
        this.lastName = (String) map.get(lastNameKey);
        this.firstName = (String) map.get(firstNameKey);
        this.userId = String.valueOf( map.get(idKey));
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
