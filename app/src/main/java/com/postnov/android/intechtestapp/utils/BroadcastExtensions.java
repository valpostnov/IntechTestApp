package com.postnov.android.intechtestapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.postnov.android.intechtestapp.player.PlaybackService;

/**
 * Created by platon on 15.06.2016.
 */
public class BroadcastExtensions
{
    public static void registerLBReceiver(Context context, BroadcastReceiver receiver)
    {
        IntentFilter statusFilter = new IntentFilter(PlaybackService.BROADCAST_ACTION);
        statusFilter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, statusFilter);
    }

    public static void unregisterLBReceiver(Context context, BroadcastReceiver receiver)
    {
        if (receiver != null)
        {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
        }
    }
}
