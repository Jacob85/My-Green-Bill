package com.mygreenbill.common;

import org.json.JSONObject;

/**
 * Created by Jacob on 3/22/14.
 */
public class RequestJson
{
    private JSONObject request;
    private int numberOfResendingAttempts;
    private int messageId;

    public RequestJson(JSONObject request, int numberOfResendingAttempts)
    {
        this.request = request;
        this.numberOfResendingAttempts = numberOfResendingAttempts;
    }

    public RequestJson(JSONObject request, int numberOfResendingAttempts, int messageId)
    {
        this.request = request;
        this.numberOfResendingAttempts = numberOfResendingAttempts;
        this.messageId = messageId;
    }

    public RequestJson()
    {
    }

    public int getMessageId()
    {
        return messageId;
    }

    public void setMessageId(int messageId)
    {
        this.messageId = messageId;
    }

    public JSONObject getRequest()
    {
        return request;
    }

    public void setRequest(JSONObject request)
    {
        this.request = request;
    }

    public int getNumberOfResendingAttempts()
    {
        return numberOfResendingAttempts;
    }

    public void setNumberOfResendingAttempts(int numberOfResendingAttempts)
    {
        this.numberOfResendingAttempts = numberOfResendingAttempts;
    }
    public void incrementResendAttempt()
    {
        numberOfResendingAttempts ++;
    }

    @Override
    public String toString()
    {
        return request.toString();
    }
}
