package com.mygreenbill.common;

import com.mygreenbill.registration.RegistrationRequestAbstract;
import com.mygreenbill.security.EncryptionType;
import org.omg.PortableInterceptor.ServerRequestInfo;

import java.util.ArrayList;
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
    private String forwardEmail;
    private boolean isActive;
    private List<GreenBillCompany> userCompanyList;

    //database filed
    private final String emailKey = "email";
    private final String firstNameKey = "first_name";
    private final String idKey = "id";
    private final String lastNameKey = "last_name";
    private final String passwordKey = "password";
    private final String isActiveKey = "is_active";
    private final String forwardEmailKey = "forward_email";

    public GreenBillUser()
    {
    }

    public GreenBillUser(String firstName, String lastName, String userId, String password, String email, List<GreenBillCompany> userCompanyList)
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
        this.forwardEmail = this.email;
    }

    public GreenBillUser(Map<String, Object> map)
    {
        this.email = (String) map.get(emailKey);
        this.password = (String) map.get(passwordKey);
        this.lastName = (String) map.get(lastNameKey);
        this.firstName = (String) map.get(firstNameKey);
        this.userId = String.valueOf( map.get(idKey));
        this.isActive = (Boolean) map.get(isActiveKey);
        this.forwardEmail = (String) map.get(forwardEmailKey);
    }

    public String getForwardEmail()
    {
        return forwardEmail;
    }

    public void setForwardEmail(String forward_email)
    {
        this.forwardEmail = forward_email;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
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

    public List<GreenBillCompany> getUserCompanyList()
    {
        return userCompanyList;
    }

    public void setUserCompanyList(List<GreenBillCompany> userCompanyList)
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

    public void addCompany(GreenBillCompany toAdd)
    {
        if (userCompanyList == null)
            userCompanyList = new ArrayList<GreenBillCompany>();

        userCompanyList.add(toAdd);
    }

    public boolean isUserRegisteredToCompany(String companyId)
    {
        for (GreenBillCompany company : userCompanyList)
        {
            if (String.valueOf(company.getId()).equals(companyId))
                return true;
        }
        return false;
    }

    public GreenBillCompany getCompanyById(String companyId)
    {
        for (GreenBillCompany company : userCompanyList)
            if (companyId.equals(String.valueOf(company.getId())))
                return company;

        return null;
    }
}
