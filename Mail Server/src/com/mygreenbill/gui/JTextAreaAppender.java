package com.mygreenbill.gui;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;

/**
 * Created by ipeleg on 4/11/14.
 */
public class JTextAreaAppender extends AppenderSkeleton
{
    private JTextArea textArea;

    public JTextAreaAppender(JTextArea textArea)
    {
        this.textArea = textArea;
    }

    @Override
    protected void append(LoggingEvent loggingEvent)
    {
        textArea.append(loggingEvent.getRenderedMessage() + "\n");
    }

    @Override
    public void close()
    {

    }

    @Override
    public boolean requiresLayout()
    {
        return false;
    }
}
