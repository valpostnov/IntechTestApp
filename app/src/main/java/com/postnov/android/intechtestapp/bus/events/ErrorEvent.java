package com.postnov.android.intechtestapp.bus.events;

/**
 * Created by platon on 16.06.2016.
 */
public class ErrorEvent
{
    private String mError;

    public ErrorEvent(String error)
    {
        mError = error;
    }

    public String getError()
    {
        return mError;
    }
}
