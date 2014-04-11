package com.mygreenbill.messages;

import com.mygreenbill.security.EncryptionType;
import com.mygreenbill.security.EncryptionUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ipeleg on 4/11/14.
 */
public class AddNewUserMessage
{
    private final Logger LOGGER = Logger.getLogger(AddNewUserMessage.class);

    private String username;
    private String forwardAddress;
    private String password;

    public AddNewUserMessage(JSONObject message)
    {
        try
        {
            forwardAddress = message.getString("forwardAddress");
            password = message.getString("password");

            // The username is the MD5 of the user real address
            username = EncryptionUtil.encryptString(forwardAddress, EncryptionType.MD5);
        }
        catch (JSONException e)
        {
            LOGGER.error("JSONException Error in AddNewUserMessage: " + e.getMessage());
        }
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getForwardAddress()
    {
        return forwardAddress;
    }

    public void setForwardAddress(String forwardAddress)
    {
        this.forwardAddress = forwardAddress;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
