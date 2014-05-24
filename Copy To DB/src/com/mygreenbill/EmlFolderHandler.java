package com.mygreenbill;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ipeleg on 3/24/14.
 * Class for handling the folder which contain the EML file and the attachments
 */
public class EmlFolderHandler
{
    //Create class logger
    private static final Logger LOGGER = Logger.getLogger(EmlFolderHandler.class);
    private MimeMessage emlFile;
    private ArrayList<File> files;

    public EmlFolderHandler(String emlPath)
    {
        File emlFolder = new File(emlPath);
        files = new ArrayList<File>(Arrays.asList(emlFolder.listFiles())); // Get the folder content files

        for (File file : files)
        {
            if (FilenameUtils.getExtension(file.getName()).equals("eml"))
            {
                try
                {
                    InputStream source = new FileInputStream(file);
                    emlFile = new MimeMessage(null, source);
                }
                catch (FileNotFoundException e)
                {
                    LOGGER.error("FileNotFoundException in EmlFolderHandler");
                    LOGGER.error(e.getMessage());
                }
                catch (MessagingException e)
                {
                    LOGGER.error("MessagingException in EmlFolderHandler");
                    LOGGER.error(e.getMessage());
                }

                LOGGER.info("EML file was found and loaded");
            }
        }
    }

    /**
     * Delete all the attachments except for the EML file
     */
    public void deleteAttachments()
    {
        for (File file : files)
        {
            if (!FilenameUtils.getExtension(file.getName()).equals("eml"))
            {
                try
                {
                    if (file.delete())
                        LOGGER.info(file.getName() + " was deleted");
                }
                catch (Exception e)
                {
                    LOGGER.error("Exception in deleteAttachments");
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }

    /**
     * Returns the files in the folder as ArrayList
     * @return Return the files array
     */
    public ArrayList<File> getFiles()
    {
        return files;
    }

    /**
     * Getting the "TO" header from the EML file which the account name is part of
     * @return Return the TO header from the EML file
     */
    public String getToHeader()
    {
        String TO = "";

        try
        {
            TO = String.valueOf(emlFile.getRecipients(Message.RecipientType.TO)[0]);
            TO = TO.substring(TO.indexOf("<")+1, TO.lastIndexOf(">"));
        }
        catch (MessagingException e)
        {
            LOGGER.error("MessagingException in getToHeader");
            LOGGER.error(e.getMessage());
        }

        return TO;
    }

    /**
     * Getting the "FROM" header from the EML file which the account name is part of
     * @return Return the FROM header from the EML file
     */
    public String getFromHeader()
    {
        String FROM = "";

        try
        {
            FROM = String.valueOf(emlFile.getFrom()[0]);
            FROM = FROM.substring(FROM.indexOf("<")+1, FROM.lastIndexOf(">"));
        }
        catch (MessagingException e)
        {
            LOGGER.error("MessagingException in getFromHeader");
            LOGGER.error(e.getMessage());
        }

        return FROM;
    }

    /**
     * Getting the "SUBJECT" header from the EML file which the account name is part of
     * @return Return the SUBJECT header from the EML file
     */
    public String getSubjectHeader()
    {
        String SUBJECT = "";

        try
        {
            SUBJECT = emlFile.getSubject();
        }
        catch (MessagingException e)
        {
            LOGGER.error("MessagingException in getSubjectHeader");
            LOGGER.error(e.getMessage());
        }

        return SUBJECT;
    }

    /**
     * Getting the content (BODY) from the EML file which the account name is part of
     * @return Return the CONTENT header from the EML file
     */
    public String getEmailContent()
    {
        String CONTENT = "";

        try
        {
            Multipart mp = (Multipart)emlFile.getContent();
            for (int i = 0; i < mp.getCount(); i++)
            {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain") || bp.isMimeType("text/html"))
                    CONTENT = (String) bp.getContent();
            }
        }
        catch (MessagingException e)
        {
            LOGGER.error("MessagingException in getEmailContent");
            LOGGER.error(e.getMessage());
        }
        catch (IOException e)
        {
            LOGGER.error("IOException in getEmailContent");
            LOGGER.error(e.getMessage());
        }

        return CONTENT;
    }

    /**
     * Stripping the account name from the "TO" header -> "accountName@mygreenbill.ssh"
     * @return Return the account name
     */
    public String getAccountName()
    {
        String account = getToHeader();
        return account.substring(0, account.indexOf("@"));
    }
}
