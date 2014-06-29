package com.mygreenbill.messages;

import com.mygreenbill.Exceptions.DatabaseException;
import com.mygreenbill.Exceptions.InitException;
import com.mygreenbill.IMailServerHandler;
import com.mygreenbill.MailServerHandler;
import com.mygreenbill.common.ConnectionManager;
import com.mygreenbill.common.JsonRequestFields;
import com.mygreenbill.common.MessageType;
import com.mygreenbill.database.DatabaseHandler;
import com.mygreenbill.security.EncryptionType;
import com.mygreenbill.security.EncryptionUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonMessageHandler
{
    private final Logger LOGGER = Logger.getLogger(JsonMessageHandler.class);

    private IMailServerHandler mailServerHandler;
    private DatabaseHandler databaseHandler;
    private int maxNumOfTries = 5;

    private CacheManager cacheManager;
    private Cache msgCache;
    private long cacheTtlSeconds = 60 * 5; // The TTL in seconds for each element in the cache
    private final String cacheName = "IncomingMessagesCache"; // The name of the cache
    
    public JsonMessageHandler()
    {
    	mailServerHandler = new MailServerHandler();
        databaseHandler = DatabaseHandler.getInstance();
        cacheManager = CacheManager.getInstance();

        // Checking if the cache already exist
        if (!cacheManager.cacheExists(cacheName))
        {
            msgCache = new Cache(cacheName, 1000, false, false, cacheTtlSeconds, cacheTtlSeconds);
            cacheManager.addCache(msgCache);
        }
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
            JSONObject innerJson = json.getJSONObject(JsonRequestFields.MESSAGE.field()); // Getting the inner JSON object
            int messageId = innerJson.getInt(JsonRequestFields.MESSAGE_ID.field()); // Getting the message ID
            sendAckMessage(messageId); // Sending back ACK message for every incoming message

            // If the message was already received return
            if (isMessageAlreadyReceived(messageId))
                return;

            handleMessage(innerJson); // Handling the inner JSON (The Message)
        }
        catch (JSONException e)
        {
            LOGGER.error("JSONException in processJson");
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
            String messageType = innerJson.getString(JsonRequestFields.MESSAGE_TYPE.field());

            // Preform an action according to message type
            switch (MessageType.valueOf(messageType))
            {
                case ADD_USER:
                    addNewAccount(innerJson.getString(JsonRequestFields.ACCOUNT_NAME.field()), innerJson.getString(JsonRequestFields.PASSWORD.field()), innerJson.getString(JsonRequestFields.EMAIL_ADDRESS.field()));
                    break;

                case SET_NEW_FORWARD_ADDRESS:
                    setNewForwardAddress(innerJson.getString(JsonRequestFields.ACCOUNT_NAME.field()), innerJson.getString(JsonRequestFields.FORWARD_ADDRESS.field()));
                    break;

                case SEND_MAIL_TO_COSTUMER:
                    sendMessageToCostumer(innerJson.getString(JsonRequestFields.EMAIL_SEND_TO.field()), innerJson.getString(JsonRequestFields.EMAIL_SUBJECT.field()), innerJson.getString(JsonRequestFields.MESSAGE_CONTENT.field()));
                    break;

                default:
                    LOGGER.info("Message with ID: " + innerJson.getString(JsonRequestFields.MESSAGE_ID.field()) + " has no valid MessageType -> " + innerJson.getString(JsonRequestFields.MESSAGE_TYPE.field()));
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
     * Sending ACK message for the given ID
     * @param messageId The ID of the message to send ACK on
     */
    public void sendAckMessage(int messageId)
    {
        try
        {
            JSONObject ackJson = new JSONObject("{messageId: "+ messageId +", MessageType: ACK}");
            LOGGER.info("Sending ACK on message ID: " + messageId);
            ConnectionManager connectionManager = ConnectionManager.getInstance();
            connectionManager.sendToTrafficBlade(ackJson);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (InitException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Checking if the message id is already exist in the local cache, if not the message is new and will be added
     * to the local cache
     * @param messageId The message ID to check
     * @return True if the message already exist, false otherwise
     */
    public boolean isMessageAlreadyReceived(int messageId)
    {
        Element newElement = new Element(messageId, messageId);

        // If the newElement was found the message already received, else the message is new
        if (msgCache.get(messageId) != null)
        {
            LOGGER.info("The message with ID: " + messageId + " was already received");
            return true;
        }
        else
        {
            LOGGER.info("Adding message ID: " + messageId + " to cache");
            msgCache.put(newElement); // Adding the message to cache
            return false;
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
            JSONObject innerJson = json.getJSONObject(JsonRequestFields.MESSAGE.field());
            id = innerJson.getInt(JsonRequestFields.MESSAGE_ID.field()); // Getting the message ID
            String messageMD5 = getMD5(String.valueOf(innerJson.toString().length())); // Checking the MD5 of the incoming message

            if (messageMD5.equals(json.getString(JsonRequestFields.CHECK_SUM.field()))) // If the MD5 field is equal to the MD5 of the message return true
            {
                LOGGER.info("Message with ID: " + id + " was fully received :)");
                return true;
            }
            else
            {
                LOGGER.info("Message with ID: " + id + " was not fully received :(");
                LOGGER.info("Message MD5 CheckSum filed: " + json.getString(JsonRequestFields.CHECK_SUM.field()));
                LOGGER.info("Message received MD5: " + messageMD5 + " String: " + innerJson.toString());
            }
        }
        catch (JSONException e)
        {
            LOGGER.error("JSONException in checkMessageMD5");
            LOGGER.error(e.getMessage());
        }

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

    public void sendMessageToCostumer(String toEmail, String subject, String messageContent)
    {
        int numOfTries = 0;
        while (!mailServerHandler.sendMessage(toEmail, subject, messageContent))
        {
            ++numOfTries;
            if (numOfTries >= maxNumOfTries)
            {
                LOGGER.error("Max number of tries reached, could not send message to " + toEmail);
                break;
            }
        }
    }
}
