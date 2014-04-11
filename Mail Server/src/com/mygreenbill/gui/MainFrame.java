package com.mygreenbill.gui;

import com.mygreenbill.Exceptions.InitException;
import com.mygreenbill.common.ConnectionManager;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ipeleg on 4/11/14.
 */
public class MainFrame extends JFrame
{
    //Create class logger
    private static final Logger LOGGER = Logger.getLogger(MainFrame.class);

    private JTextArea textArea;
    private JButton startButton;
    private JButton stopButton;

    public MainFrame()
    {
        setTitle("Mail Blade Console");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setButtons();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void setButtons()
    {
        JPanel panel = new JPanel();
        getContentPane().add(panel);

        panel.setLayout(null);

        startButton = new JButton("Start App");
        startButton.setBounds(10, 10, 100, 30);
        startButton.setToolTipText("Starting the application client and server threads");

        startButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                try
                {
                    textArea.setText("");
                    ConnectionManager connectionManager = ConnectionManager.getInstance();
                    startButton.setEnabled(false);
                }
                catch (InitException e)
                {
                    e.printStackTrace();
                }
            }
        });

        stopButton = new JButton("Quit");
        stopButton.setBounds(10, 50, 100, 30);
        stopButton.setToolTipText("Exiting the application");

        stopButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                System.exit(0);
            }
        });

        textArea = new JTextArea("Hello,\nplease click the start button in order to start the application.");
        textArea.setBounds(120, 10, 450, 350);
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        Logger.getRootLogger().addAppender(new JTextAreaAppender(textArea));

        panel.add(startButton);
        panel.add(stopButton);
        panel.add(textArea);
    }
}
