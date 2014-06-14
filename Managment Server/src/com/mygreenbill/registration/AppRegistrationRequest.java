package com.mygreenbill.registration;

import com.mygreenbill.common.GeneralUtilities;
import com.mygreenbill.security.EncryptionType;
import com.mygreenbill.security.EncryptionUtil;

/**
 * Created by Jacob on 6/12/14.
 */
public class AppRegistrationRequest extends RegistrationRequestAbstract
{
    public static final String defaultPassword = "Default Password";
    private String pictureUrl;

    public AppRegistrationRequest(String id, String email, String pictureUrl)
    {
        super(id, email);
        this.pictureUrl = pictureUrl;
    }

    public String getPictureUrl()
    {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl)
    {
        this.pictureUrl = pictureUrl;
    }


    @Override
    public String getEncryptPassword(EncryptionType encryptionType)
    {
        return EncryptionUtil.encryptString(defaultPassword, encryptionType);
    }

    @Override
    public boolean isRequestValid()
    {
        if (GeneralUtilities.hasData(email) && GeneralUtilities.hasData(id))
            return true;
        return false;
    }

    @Override
    public String toString()
    {
        return super.toString() + " AppRegistrationRequest{" +
                "pictureUrl='" + pictureUrl + '\'' +
                '}';
    }
}
