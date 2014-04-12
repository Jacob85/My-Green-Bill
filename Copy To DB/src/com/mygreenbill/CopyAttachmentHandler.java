package com.mygreenbill;

import com.mygreenbill.Exceptions.DatabaseException;
import com.mygreenbill.database.DatabaseHandler;
import com.mygreenbill.ssh.ConnectionHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by ipeleg on 3/24/14.
 * Class for handling attachments copy to the DB machine
 */
public class CopyAttachmentHandler
{
    //Create class logger
    private static final Logger LOGGER = Logger.getLogger(CopyAttachmentHandler.class);

    private Properties prop = new Properties();
    private ConnectionHandler connectionHandler;
    private EmlFolderHandler emlParser;
    private DatabaseHandler databaseHandler;

    public CopyAttachmentHandler(EmlFolderHandler emlParser)
    {
        try
        {
            this.emlParser = emlParser;
            prop.load(CopyAttachmentHandler.class.getResourceAsStream("/conf/configuration.properties")); // Load the file to the properties object
            connectionHandler = new ConnectionHandler();
            connectionHandler.createConnection(prop.getProperty("mysql_username"), prop.getProperty("mysql_password"), prop.getProperty("mysql_ip"));

            databaseHandler = DatabaseHandler.getInstance();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.error("IOException in CopyAttachmentHandler");
            LOGGER.error(e.getMessage());
        }
    }

    public void copyAttachmentsToDb(String accountName)
    {
        ArrayList<File> files = emlParser.getFiles();
        connectionHandler.changeFolderOnRemote(prop.getProperty("mysql_path") + accountName + "/");

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
            String FROM = emlParser.getFromHeader();

            List result = databaseHandler.runGetQuery("SELECT id FROM company WHERE email ='" + FROM + "';");
            String FROM_ID = "";
            String TO = emlParser.getToHeader();
            String TO_ID = emlParser.getToHeader();

            String path = prop.getProperty("mysql_path") + accountName + "/" + file.getName();

            databaseHandler.runInsertQuery("CALL NewMassage(" + FROM + "," + FROM_ID + "," + TO + "," + TO_ID +");");
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
