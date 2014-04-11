package com.mygreenbill.gui;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

/**
 * Created by ipeleg on 4/11/14.
 */
public class JTextAreaAppender extends AppenderSkeleton
{
    private JTextArea textArea;
    private boolean isAutoScroll;

    public JTextAreaAppender(JTextArea textArea)
    {
        this.textArea = textArea;
        isAutoScroll = true;
    }

    @Override
    protected void append(LoggingEvent loggingEvent)
    {
        textArea.append(loggingEvent.getRenderedMessage() + "\n");
        if (isAutoScroll)
            textArea.setCaretPosition(textArea.getDocument().getLength());
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

    public boolean isAutoScroll()
    {
        return isAutoScroll;
    }

    public void setAutoScroll(boolean isAutoScroll)
    {
        this.isAutoScroll = isAutoScroll;
    }
}
