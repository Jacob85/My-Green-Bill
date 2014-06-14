package com.mygreenbill.registration;

import com.mygreenbill.security.EncryptionType;
import com.mygreenbill.security.EncryptionUtil;

import java.io.Serializable;

/**
 * Created by Jacob on 3/16/14.
 */
public class FullRegistrationRequest extends RegistrationRequestAbstract implements Serializable
{
    private String password;

    public FullRegistrationRequest(String id, String email, String password)
    {
        super(id, email);
        /*The Password will be saved as unEncrypted format here and will be encrypt every time the user want to get the password*/
        /*This is because the user might want to Encrypt the password in different formats */
        this.password = password;
       // this.password = EncryptionUtil.encryptString(password, EncryptionType.MD5);
    }

    public void setPassword(String password)
    {
        this.password = password;
    }


    @Override
    public String getEncryptPassword(EncryptionType encryptionType)
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
        return super.toString() + " FullRegistrationRequest{" +
                "password='" + password + '\'' +
                '}';
    }
}
