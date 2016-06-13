package com.postnov.android.intechtestapp.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.postnov.android.intechtestapp.utils.Const;
import com.postnov.android.intechtestapp.utils.Utils;

import static com.postnov.android.intechtestapp.player.PlaybackService.EXTENDED_DATA_STATUS;

/**
 * Created by platon on 13.06.2016.
 */
public class PlaybackStateReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        int statusCode = intent.getIntExtra(EXTENDED_DATA_STATUS, -1);
        switch (statusCode)
        {
            default:
                Utils.showToast(context, Const.ERROR_UNKNOWN);
        }
    }
}
