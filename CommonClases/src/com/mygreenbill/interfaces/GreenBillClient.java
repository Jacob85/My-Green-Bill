package com.mygreenbill.interfaces;

import com.mygreenbill.common.RequestJson;

/**
 * Created by Jacob on 3/28/14.
 */
public interface GreenBillClient extends Runnable
{
    public void sendMessage(RequestJson message);
}
