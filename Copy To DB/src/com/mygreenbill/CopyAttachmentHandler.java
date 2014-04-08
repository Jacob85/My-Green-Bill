package com.mygreenbill;

import com.mygreenbill.ssh.ConnectionHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by ipeleg on 3/24/14.
 */
public class CopyAttachmentHandler
{
    //Create class logger
    private static final Logger LOGGER = Logger.getLogger(CopyAttachmentHandler.class);

    private Properties prop = new Properties();
    private ConnectionHandler connectionHandler;
    private EmlFolderHandler emlParser;

    public CopyAttachmentHandler(EmlFolderHandler emlParser)
    {
        try
        {
            this.emlParser = emlParser;
            prop.load(CopyAttachmentHandler.class.getResourceAsStream("/conf/configuration.properties")); // Load the file to the properties object
            connectionHandler = new ConnectionHandler();
            connectionHandler.createConnection(prop.getProperty("mysql_username"), prop.getProperty("mysql_password"), prop.getProperty("mysql_ip"));
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

        for (int i=0 ; i<files.size() ; ++i)
        {
            if (FilenameUtils.getExtension(files.get(i).getName()).equals("pdf"))
            {
                connectionHandler.copyFileToRemote(files.get(i));
                LOGGER.info("File " + files.get(i).getName() + " was copied to remote machine");
            }
        }
    }

    public void closeConnection()
    {
        connectionHandler.closeConnection();
    }
}
