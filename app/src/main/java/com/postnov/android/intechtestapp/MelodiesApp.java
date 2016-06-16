package com.postnov.android.intechtestapp;

import android.app.Application;

import com.postnov.android.intechtestapp.bus.RxBus;

/**
 * Created by platon on 17.06.2016.
 */
public class MelodiesApp extends Application
{
    private static RxBus mRxBus;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mRxBus = new RxBus();
    }

    public static RxBus getEventBus()
    {
        return mRxBus;
    }
}
