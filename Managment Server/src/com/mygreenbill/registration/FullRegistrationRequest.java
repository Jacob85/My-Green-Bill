package com.mygreenbill.registration;

import com.mygreenbill.security.EncryptionType;
import com.mygreenbill.security.EncryptionUtil;

import java.io.Serializable;

/**
 * Created by Jacob on 3/16/14.
 */
public class FullRegistrationRequest extends RegistrationRequestAbstract implements Serializable
{
    private String email;
    private String password;

    public FullRegistrationRequest(String id, String email, String password)
    {
        super(id);
        this.email = email;
        this.password = EncryptionUtil.encryptString(password, EncryptionType.MD5);
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

    @Override
    public String getEncriptPassword(EncryptionType encryptionType)
    {
         return EncryptionUtil.encryptString(this.password, encryptionType);
    }

    @Override
    public boolean isRequestValid()
    {
        if (email == null || email.isEmpty())
            return false;
        if (password == null || password.isEmpty())
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "FullRegistrationRequest{" +
                "'id='" +id + '\'' +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
