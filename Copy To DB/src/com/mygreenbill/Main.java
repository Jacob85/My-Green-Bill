package com.mygreenbill;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by ipeleg on 3/24/14.
 * This standalone application will be activated for each incoming mail for any user and will
 * copy all of the attachments from the incoming mail to the appropriate folder in the DB machine
 *
 * The app should get as an argument the folder which contain the attachments files
 */
public class Main
{
    //Create class logger
    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args)
    {
        Properties prop = new Properties();

        // Checking if no args were send to the application
        if (args.length == 0)
        {
            LOGGER.error("No args were sent to the application");
            return;
        }

        try
        {
            prop.load(CopyAttachmentHandler.class.getResourceAsStream("/conf/configuration.properties")); // Load the file to the properties object
            LOGGER.info("Processing files in " + prop.getProperty("hmailserver_folder") + args[0]);

            EmlFolderHandler emlFolderHandler = new EmlFolderHandler(prop.getProperty("hmailserver_folder") + args[0]); // Getting the eml file
            CopyAttachmentHandler copyAttachmentHandler = new CopyAttachmentHandler(emlFolderHandler); // Create new attachment handler from the eml parser
            copyAttachmentHandler.copyAttachmentsToDb(emlFolderHandler.getAccountName()); // Copy the attachments for the given account to the DB machine
            copyAttachmentHandler.closeConnection(); // Close the connection with the DB machine

            emlFolderHandler.deleteAttachments(); // Delete all the attachments in the folder
        }
        catch (IOException e)
        {
            LOGGER.error("IOException in main");
            LOGGER.error(e.getMessage());
        }

        LOGGER.info("Files were copied");
    }
}
