package com.mygreenbill;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ipeleg on 3/24/14.
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

        for (int i=0 ; i<files.size() ; ++i)
        {
            if (FilenameUtils.getExtension(files.get(i).getName()).equals("eml"))
            {
                try
                {
                    InputStream source = new FileInputStream(files.get(i));
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
        for (int i=0 ; i<files.size() ; ++i)
        {
            if (!FilenameUtils.getExtension(files.get(i).getName()).equals("eml"))
            {
                try
                {
                    if (files.get(i).delete())
                        LOGGER.info(files.get(i).getName() + " was deleted");
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
     * @return
     */
    public ArrayList<File> getFiles()
    {
        return files;
    }

    /**
     * Getting the "TO" header from the EML file which the account name is part of
     * @return
     */
    public String getToHeader()
    {
        String TO = "";

        try
        {
            TO = String.valueOf(emlFile.getRecipients(Message.RecipientType.TO)[0]);
        }
        catch (MessagingException e)
        {
            LOGGER.error("MessagingException in getToHeader");
            LOGGER.error(e.getMessage());
        }

        return TO;
    }

    /**
     * Stripping the account name from the "TO" header -> "accountName@mygreenbill.ssh"
     * @return
     */
    public String getAccountName()
    {
        String account = getToHeader();
        return account.substring(1, account.indexOf("@"));
    }
}
