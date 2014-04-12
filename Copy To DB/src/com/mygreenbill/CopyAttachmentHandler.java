package com.mygreenbill;

import com.mygreenbill.Exceptions.ConfigurationException;
import com.mygreenbill.Exceptions.DatabaseException;
import com.mygreenbill.common.ConfigurationManager;
import com.mygreenbill.database.DatabaseHandler;
import com.mygreenbill.database.DatabaseHandler.MessageStatus;
import com.mygreenbill.ssh.ConnectionHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by ipeleg on 3/24/14.
 * Class for handling attachments copy to the DB machine
 */
public class CopyAttachmentHandler
{
    //Create class logger
    private static final Logger LOGGER = Logger.getLogger(CopyAttachmentHandler.class);

    private ConnectionHandler connectionHandler;
    private EmlFolderHandler emlParser;
    private DatabaseHandler databaseHandler;
    private ConfigurationManager configurationManager;
    private List<Map> result;

    public CopyAttachmentHandler(EmlFolderHandler emlParser)
    {
        try
        {
            this.emlParser = emlParser;
            configurationManager = ConfigurationManager.getInstance();
            connectionHandler = new ConnectionHandler();
            connectionHandler.createConnection(configurationManager.getProperty("mysql_username"), configurationManager.getProperty("mysql_password"), configurationManager.getProperty("mysql_ip"));

            databaseHandler = DatabaseHandler.getInstance();
        }
        catch (ConfigurationException e)
        {
            LOGGER.error("ConfigurationException in CopyAttachmentHandler");
            LOGGER.error(e.getLocalizedMessage());
        }
    }

    public void copyAttachmentsToDb(String accountName)
    {
        ArrayList<File> files = emlParser.getFiles();
        connectionHandler.changeFolderOnRemote(configurationManager.getProperty("mysql_path") + accountName + "/");

        // Go over all the attachments
        for (File file : files)
        {
            if (FilenameUtils.getExtension(file.getName()).equals("pdf"))
            {
                connectionHandler.copyFileToRemote(file);
                LOGGER.info("File " + file.getName() + " was copied to remote machine");

                updateDB(accountName, file);
            }
        }
    }

    public void updateDB(String accountName, File file)
    {
        try
        {
            DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

            String FROM = emlParser.getFromHeader();
            result = databaseHandler.runGetQuery("SELECT id FROM company WHERE email='" + FROM + "';");
            String FROM_ID = String.valueOf(result.get(0).get("id"));

            String TO = emlParser.getToHeader();
            result = databaseHandler.runGetQuery("SELECT id FROM user WHERE hmail_account_name='" + accountName + "';");
            String TO_ID = String.valueOf(result.get(0).get("id"));

            String SUBJECT = emlParser.getSubjectHeader();
            String CONTENT = "TESTING CONTENT"; //emlParser.getEmailContent();
            String PATH = configurationManager.getProperty("mysql_path") + accountName + "/" + file.getName();

            databaseHandler.runInsertQuery("CALL NewMassage('" + FROM + "','" + FROM_ID + "','" + TO + "','" + TO_ID + "','" + null + "','" + SUBJECT + "','" + CONTENT + "','" + MessageStatus.sent + "','" + file.getName() + "','" + PATH +"');");

            LOGGER.info("Message for " + TO + " was added to DB");
        }
        catch (DatabaseException e)
        {
            LOGGER.error("DatabaseException in updateDB");
            LOGGER.error(e.getMessage());
        }
    }

    public void closeConnection()
    {
        connectionHandler.closeConnection();
    }
}
