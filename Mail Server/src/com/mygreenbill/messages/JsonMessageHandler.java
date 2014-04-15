package com.mygreenbill.messages;

import com.mygreenbill.Exceptions.DatabaseException;
import com.mygreenbill.Exceptions.InitException;
import com.mygreenbill.IMailServerHandler;
import com.mygreenbill.MailServerHandler;
import com.mygreenbill.common.ConnectionManager;
import com.mygreenbill.common.MessageType;
import com.mygreenbill.database.DatabaseHandler;
import com.mygreenbill.security.EncryptionType;
import com.mygreenbill.security.EncryptionUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonMessageHandler
{
    // ENUM which represent all the possible messages types
    
    private final Logger LOGGER = Logger.getLogger(JsonMessageHandler.class);
    private IMailServerHandler mailServerHandler;
    private DatabaseHandler databaseHandler;
    
    public JsonMessageHandler()
    {
    	mailServerHandler = new MailServerHandler();
        databaseHandler = DatabaseHandler.getInstance();
    }
    
    /**
     * Processing JSON of an incoming message from the the Management-Blade.
     * This method should be called after the MD5 of the message was verified.
     * The ACK JSON will also be sent by this method.
     * @param json The incoming JSON to parse
     */
    public void processJson(JSONObject json)
    {
    	// If the JSON is NULL return
        if (json == null)
            return;

        try
        {
            JSONObject innerJson = json.getJSONObject("Message"); // Getting the inner JSON object
            int id = innerJson.getInt("messageId"); // Getting the message ID

            // Sending back the ACK json the the management blade
            JSONObject ackJson = new JSONObject("{messageId: "+ id +", MessageType: ACK}");
            LOGGER.info("Sending ACK on message ID: " + id);
            ConnectionManager connectionManager = ConnectionManager.getInstance();
            connectionManager.sendToTrafficBlade(ackJson);

            // Now handling the inner JSON
            handleMessage(innerJson);
        }
        catch (JSONException e)
        {
            LOGGER.error("JSONException in processJson");
            LOGGER.error(e.getMessage());
        }
        catch (InitException e)
		{
        	LOGGER.error("InitException in processJson");
            LOGGER.error(e.getMessage());
		}
    }

    /**
     * Handling the inner JSON which is the message itself
     * @param innerJson The inner JSON of the whole message which contain the message type.
     */
    public void handleMessage(JSONObject innerJson)
    {
        try
        {
            // Getting the message type
            String messageType = innerJson.getString("MessageType");

            // Preform an action according to message type
            switch (MessageType.valueOf(messageType))
            {
                case ADD_USER:
                    addNewAccount(innerJson.getString("accountName"), innerJson.getString("password"), innerJson.getString("address"));
                    break;

                case SET_NEW_FORWARD_ADDRESS:
                    setNewForwardAddress(innerJson.getString("accountName"), innerJson.getString("newAddress"));
                    break;
            }
        }
        catch (JSONException e)
        {
            LOGGER.error("JSONException in handleMessage");
            LOGGER.error(e.getMessage());
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
            String messageMD5 = getMD5(innerJson.toString()); // Checking the MD5 of the incoming message

            if (messageMD5.equals(json.getString("CheckSum"))) // If the MD5 field is equal to the MD5 of the message return true
            {
                LOGGER.info("Message with ID: " + id + "was fully received");
                return true;
            }
        }
        catch (JSONException e)
        {
            LOGGER.error("JSONException in checkMessageMD5");
            LOGGER.error(e.getMessage());
        }

        LOGGER.info("Message with ID: " + id + "was not fully received");
        return false;
    }

    /**
     * Calculating the MD5 of the given string
     * @param string The string on which MD5 hash wil be preformed
     * @return return MD5 of the given string
     */
    public String getMD5(String string)
    {
        return EncryptionUtil.encryptString(string, EncryptionType.MD5);
    }

    /**
     * Setting new forward address for a certain user
     * @param accountName The account name
     * @param newEmail The new forward address for the given account
     */
    public void setNewForwardAddress(String accountName, String newEmail)
    {
        mailServerHandler.setNewForwardAddress(accountName, newEmail);
    }
    
    /**
     * Creating new account in the hMailServer
     * @param accountName The account name
     * @param address The email of the new account to which email will be forwarded
     * @param password The new account password
     */
    public void addNewAccount(String accountName, String password, String address)
    {
        try
        {
            mailServerHandler.createNewAccount(accountName, password, address);

            // Set the user as active after creating an account
            databaseHandler.runUpdateQuery("UPDATE user SET is_active='1' WHERE hmail_account_name='" + accountName +"';" );
        }
        catch (DatabaseException e)
        {
            LOGGER.error("DatabaseException in addNewAccount");
            LOGGER.error(e.getMessage());
        }
    }
}
