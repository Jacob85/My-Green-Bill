package com.mygreenbill;

import com.mygreenbill.Exceptions.ConfigurationException;
import com.mygreenbill.Exceptions.DatabaseException;
import com.mygreenbill.common.ConfigurationManager;
import com.mygreenbill.database.DatabaseHandler;
import com.mygreenbill.database.DatabaseHandler.MessageStatus;
import com.mygreenbill.ssh.ConnectionHandler;
import com.mygreenbill.statistics.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ipeleg on 3/24/14.
 * Class for handling attachments copy to the DB machine
 */
public class CopyAttachmentHandler
{
    //Create class logger
    private static final Logger LOGGER = Logger.getLogger(CopyAttachmentHandler.class);

    private static final String selectCompany = "SELECT * FROM company WHERE email='?';";
    private static final String selectUser = "SELECT * FROM user WHERE hmail_account_name='?';";
    private static final String addAnalyticToUser = "CALL AddUserAnalytic('?', '?', '?', '?');";
    private static final String addNewMessage = "CALL NewMassage('?', '?', '?', '?', '?', '?', '?', '?', '?', '?');";

    private ConnectionHandler connectionHandler;
    private EmlFolderHandler emlParser;
    private DatabaseHandler databaseHandler;
    private ConfigurationManager configurationManager;
    private ParserFactory parserFactory;

    public CopyAttachmentHandler(EmlFolderHandler emlParser)
    {
        try
        {
            this.emlParser = emlParser;
            configurationManager = ConfigurationManager.getInstance();
            connectionHandler = new ConnectionHandler();
            connectionHandler.createConnection(configurationManager.getProperty("mysql_username"), configurationManager.getProperty("mysql_password"), configurationManager.getProperty("mysql_ip"));
            parserFactory = new ParserFactory();
            databaseHandler = DatabaseHandler.getInstance();
        }
        catch (ConfigurationException e)
        {
            LOGGER.error("ConfigurationException in CopyAttachmentHandler");
            LOGGER.error(e.getLocalizedMessage());
        }
    }

    /**
     * Method for adding new analytic to user form a certain company according to the received mail attachment
     * @param file The file (Bill) to parse
     * @param companyResult The company info (raw) from the DB
     */
    public void addAnalytic(File file, List<Map> companyResult, List<Map> userResult)
    {
        try
        {
            String parserName = String.valueOf(companyResult.get(0).get("parser_name")); // Getting the parser name
            BillParser parser = parserFactory.getParser(parserName);

            // Getting all the parameters for the addAnalyticToUser query
            String userId = String.valueOf(userResult.get(0).get("id"));
            String userEmail = String.valueOf(userResult.get(0).get("email"));
            String category = parser.getCategory();
            double amount = parser.parseTotalAmountToPayFromPdf(file); // Getting the total amount of the bill
            LOGGER.info("Total amount of the bill is " + amount);

            // Creating the addAnalyticToUser query with the parameters
            String query = addAnalyticToUser.replaceFirst("\\?", userId);
            query = query.replaceFirst("\\?", userEmail);
            query = query.replaceFirst("\\?", category);
            query = query.replaceFirst("\\?", String.valueOf(amount));

            databaseHandler.runInsertQuery(query);
            LOGGER.info("New analytic was added to: " + userEmail);
        }
        catch (DatabaseException e)
        {
            LOGGER.error("DatabaseException in addAnalytic");
            LOGGER.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void updateDB(String accountName, File file, List<Map> companyResult, List<Map> userResult)
    {
        try
        {
            String FROM = emlParser.getFromHeader();
            String FROM_ID = String.valueOf(companyResult.get(0).get("id"));
            String COMPANY_NAME = String.valueOf(companyResult.get(0).get("name")).replaceAll("\\s", ""); // Remove any spaces in the company name

            String TO = String.valueOf(userResult.get(0).get("email"));
            String TO_ID = String.valueOf(userResult.get(0).get("id"));


            String SUBJECT = emlParser.getSubjectHeader();
            String CONTENT = emlParser.getEmailContent();
            String PATH = configurationManager.getProperty("mysql_path") +  accountName + "/" + COMPANY_NAME + "/" + file.getName();

            // Building the addNewMessage query
            String query = addNewMessage.replaceFirst("\\?", FROM);
            query = query.replaceFirst("\\?", FROM_ID);
            query = query.replaceFirst("\\?", TO);
            query = query.replaceFirst("\\?", TO_ID);
            query = query.replaceFirst("\\?", "");
            query = query.replaceFirst("\\?", SUBJECT);
            query = query.replaceFirst("\\?", CONTENT);
            query = query.replaceFirst("\\?", String.valueOf(MessageStatus.sent));
            query = query.replaceFirst("\\?", file.getName());
            query = query.replaceFirst("\\?", PATH);
            LOGGER.info("Query " + query  + " was build");


            databaseHandler.runInsertQuery(query);

            LOGGER.info("Message for " + TO + " was added to DB");
        }
        catch (DatabaseException e)
        {
            LOGGER.error("DatabaseException in updateDB");
            LOGGER.error(e.getLocalizedMessage());
        }
    }

    public void closeConnection()
    {
        connectionHandler.closeConnection();
    }

    public void copyAttachmentsToDb(String accountName)
    {
        List<Map> userResults = null;
        List<Map> companyResults = null;
        ArrayList<File> files = emlParser.getFiles();
        String COMPANY_NAME, FROM, query;

        try
        {
            LOGGER.info(emlParser.getFromHeader());
            FROM = emlParser.getFromHeader();

            query = selectCompany.replace("?", FROM); // Creating the query for getting the company

            LOGGER.info("Making DB query: " + query);
            companyResults = databaseHandler.runGetQuery(query); // Get the company

            query = selectUser.replace("?", accountName);
            LOGGER.info("Making DB query: " + query);
            userResults = databaseHandler.runGetQuery(query); // Get the user
        }
        catch (DatabaseException e)
        {
            LOGGER.error("DatabaseException in copyAttachmentsToDb");
            LOGGER.error(e.getLocalizedMessage());
        }

        COMPANY_NAME = String.valueOf(companyResults.get(0).get("name")).replaceAll("\\s", ""); // Remove any spaces in the company name
        connectionHandler.changeFolderOnRemote(configurationManager.getProperty("mysql_path") + accountName + "/" + COMPANY_NAME);

        // Go over all the attachments
        for (File file : files)
        {
            if (FilenameUtils.getExtension(file.getName()).equals("pdf"))
            {
                addAnalytic(file, companyResults, userResults); // Adding analytic to user
                connectionHandler.copyFileToRemote(file);
                LOGGER.info("File " + file.getName() + " was copied to remote machine");

                updateDB(accountName, file, companyResults, userResults);
            }
        }
    }
}
