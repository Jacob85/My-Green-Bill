package com.mygreenbill.gui;

import com.mygreenbill.Exceptions.InitException;
import com.mygreenbill.common.ConnectionManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ipeleg on 4/11/14.
 */
public class MainFrame extends JFrame
{
    //Create class logger
    private static final Logger LOGGER = Logger.getLogger(MainFrame.class);

    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JButton startButton;
    private JButton clearTextButton;
    private JCheckBox autoScroll;
    private JButton quitButton;
    private JTextAreaAppender textAreaAppender;

    public MainFrame()
    {
        setTitle("Mail Blade Console");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setButtons();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void setButtons()
    {
        // Creating the panel
        JPanel panel = new JPanel();
        getContentPane().add(panel);

        panel.setLayout(null);

        // Creating the start button
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
                    startButton.setEnabled(false);
                    textArea.setText("");
                    LOGGER.info("App started at: " + getCurrentTime());

                    ConnectionManager connectionManager = ConnectionManager.getInstance();
                }
                catch (InitException e)
                {
                    e.printStackTrace();
                }
            }
        });

        // Creating the clear text area button
        clearTextButton = new JButton("Clear Text");
        clearTextButton.setBounds(10, 50, 100, 30);
        clearTextButton.setToolTipText("Clearing the area text");
        clearTextButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                textArea.setText("");
            }
        });

        // Creating the auto scroll check box
        autoScroll = new JCheckBox("Auto Scroll");
        autoScroll.setSelected(true);
        autoScroll.setBounds(10, 90, 110, 30);
        autoScroll.setToolTipText("Disabling / Enabling the auto scroll");
        autoScroll.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                if (autoScroll.isSelected())
                    textAreaAppender.setAutoScroll(true);
                else
                    textAreaAppender.setAutoScroll(false);
            }
        });

        // Creating the quit button
        quitButton = new JButton("Quit");
        quitButton.setBounds(10, 130, 100, 30);
        quitButton.setToolTipText("Exiting the application");
        quitButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                LOGGER.info("App closed at: " + getCurrentTime());

                System.exit(0);
            }
        });

        // Creating the text area
        textArea = new JTextArea("Hello,\nplease click the start button in order to start the application.");
        textArea.setEditable(false);

        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(120, 10, 670, 560);
        scrollPane.setWheelScrollingEnabled(true);

        textAreaAppender = new JTextAreaAppender(textArea);

        // Setting the text area as one of the log4j appenders
        Logger.getRootLogger().addAppender(textAreaAppender);

        // Adding all the GUI components to the panel
        panel.add(startButton);
        panel.add(clearTextButton);
        panel.add(autoScroll);
        panel.add(quitButton);
        panel.add(scrollPane);
    }

    private String getCurrentTime()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
