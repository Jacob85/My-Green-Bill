package com.mygreenbill.messages;

import com.mygreenbill.Exceptions.InitException;
import com.mygreenbill.IMailServerHandler;
import com.mygreenbill.MailServerHandler;
import com.mygreenbill.common.ConnectionManager;
import com.mygreenbill.security.EncryptionType;
import com.mygreenbill.security.EncryptionUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonMessageHandler
{
    public enum MessageType {ADD_USER, SET_NEW_FORWARD_ADDRESS};
    
    private final Logger LOGGER = Logger.getLogger(JsonMessageHandler.class);
    private IMailServerHandler mailServerHandler;
    
    public JsonMessageHandler()
    {
    	mailServerHandler = new MailServerHandler();
    }
    
    /**
     * Processing JSON of an incoming message from the the Management-Blade.
     * This method should be called after the MD5 of the message was verified.
     * @param json The incoming JSON to parse
     */
    public void processJson(JSONObject json)
    {
    	// If the JSON is NULL return
        if (json == null)
            return;

        try
        {
        	ConnectionManager connectionManager = ConnectionManager.getInstance();
            JSONObject innerJson = json.getJSONObject("Message"); // Getting the inner JSON object
            int id = innerJson.getInt("messageID"); // Getting the message ID

            JSONObject ackJson = new JSONObject("{messageID: "+ id +", MessageType: ACK}");
            LOGGER.info("Sending ACK on message ID: " + id);
            connectionManager.sendToTrafficBlade(ackJson); // Sending back the ACK json the the management blade

            String messageType = innerJson.getString("MessageType"); // Getting the message type

            switch (MessageType.valueOf(messageType))
            {
                case ADD_USER:
                    addNewAccount(new AddNewUserMessage(innerJson));
                    break;

                case SET_NEW_FORWARD_ADDRESS:
                    break;
            }
        }
        catch (JSONException e)
        {
            LOGGER.error("JSONException Error in processJson: " + e.getMessage());
        }
        catch (InitException e)
		{
        	LOGGER.error("InitException Error in processJson: " + e.getMessage());
		}
    }

    /**
     * Method for checking the MD5 of the message
     * @param json The all message JSON which include the MD5 and the inner JSON (inner message)
     * @return true if the MD5 is equal and the message was fully received, false otherwise
     */
    public boolean checkMessageMD5(JSONObject json)
    {
        int id = 0;

        // If the JSON is NULL return false
        if (json == null)
            return false;

        try
        {
            JSONObject innerJson = json.getJSONObject("Message");
            id = innerJson.getInt("messageID"); // Getting the message ID
            String messageMD5 = EncryptionUtil.encryptString(innerJson.toString(), EncryptionType.MD5); // Checking the MD5 of the incoming message

            if (messageMD5.equals(json.getString("CheckSum"))) // If the MD5 field is equal to the MD5 of the message return true
            {
                LOGGER.info("Message with ID: " + id + "was fully received");
                return true;
            }
        }
        catch (JSONException e)
        {
            LOGGER.error("JSONException Error in checkMessageMD5: " + e.getMessage());
        }

        LOGGER.info("Message with ID: " + id + "was not fully received");
        return false;
    }
    
    /**
     * Creating new account in the hMailServer
     * @param addNewUserMessage The email of the new account to which email will be forwarded
     */
    public void addNewAccount(AddNewUserMessage addNewUserMessage)
    {
    	mailServerHandler.createNewAccount(addNewUserMessage.getUsername(),
                addNewUserMessage.getPassword(),
                addNewUserMessage.getForwardAddress());
    }
}
