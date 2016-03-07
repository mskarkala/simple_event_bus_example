package com.eventbusexample.mes.eventbusexample;

/**
 * Created by mes on 3/7/16.
 */
// Source: http://www.andreas-schrade.de/2015/11/28/android-how-to-use-the-greenrobot-eventbus/

// The event
public class MessageEvent
{
    private final String message;

    public MessageEvent(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

}
