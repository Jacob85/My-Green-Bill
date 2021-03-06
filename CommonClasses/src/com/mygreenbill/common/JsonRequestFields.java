package com.mygreenbill.common;

/**
 * Created by Jacob on 5/3/14.
 */
public enum JsonRequestFields
{
    MESSAGE("Message"),
    CHECK_SUM("CheckSum"),
    MESSAGE_TYPE("MessageType"),
    ACCOUNT_NAME("accountName"),
    PASSWORD("password"),
    EMAIL_ADDRESS("address"),
    EMAIL_SEND_TO("To"),
    EMAIL_SUBJECT("Subject"),
    MESSAGE_CONTENT("MessageContent"),
    MESSAGE_ID("messageId"),
    FORWARD_ADDRESS("ForwardAddress");

    private String field;
    JsonRequestFields(String field)
    {
       this.field = field;
    }

    public String field()
    {
        return field;
    }
}
