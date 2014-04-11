package com.mygreenbill;

import com.mygreenbill.gui.MainFrame;
import org.apache.log4j.Logger;

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

        // IMailServerHandler obj = new MailServerHandler();
        // obj.createNewAccount("hanny", "1234", "barhanny@gmail.com");
        // obj.setNewForwardAddress("hanny", "hannybanister@gmail.com");
        // obj.sendMessage("ipeleg@hotmail.com", "Some subject", "Hello idan\n\nThis is a testing email");
        // obj.getAccountAllAttachments("ipeleg");
    }
}
