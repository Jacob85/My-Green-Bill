package com.mygreenbill;

import com.mygreenbill.Exceptions.DatabaseException;
import com.mygreenbill.database.DatabaseHandler;
import com.mygreenbill.gui.MainFrame;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by ipeleg on 3/20/14.
 */
public class Main
{
    //Create class logger
    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args)
    {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);

        try
        {
            DatabaseHandler databaseHandler = DatabaseHandler.getInstance();
            java.util.List<Map> result = databaseHandler.runGetQuery("SELECT id FROM company WHERE email='poalim@mail.com';");
            LOGGER.info(result.get(0).get("id"));
        }
        catch (DatabaseException e)
        {
            LOGGER.error(e.getLocalizedMessage());
            e.printStackTrace();
        }

        // IMailServerHandler obj = new MailServerHandler();
        // obj.createNewAccount("hanny", "1234", "barhanny@gmail.com");
        // obj.setNewForwardAddress("hanny", "hannybanister@gmail.com");
        // obj.sendMessage("ipeleg@hotmail.com", "Some subject", "Hello idan\n\nThis is a testing email");
        // obj.getAccountAllAttachments("ipeleg");
    }
}
