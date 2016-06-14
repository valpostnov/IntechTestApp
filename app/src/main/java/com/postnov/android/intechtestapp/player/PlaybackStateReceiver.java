package com.postnov.android.intechtestapp.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.postnov.android.intechtestapp.utils.UiExtensions;

import static com.postnov.android.intechtestapp.player.PlaybackService.EXTENDED_DATA_STATUS;
import static com.postnov.android.intechtestapp.utils.Const.ERROR_NO_CONNECTION;
import static com.postnov.android.intechtestapp.utils.Const.ERROR_UNKNOWN;
import static com.postnov.android.intechtestapp.utils.Const.MSG_ERROR_NO_CONNECTION;
import static com.postnov.android.intechtestapp.utils.Const.MSG_ERROR_UNKNOWN;

/**
 * Created by platon on 15.06.2016.
 */
public class PlaybackStateReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        int statusCode = intent.getIntExtra(EXTENDED_DATA_STATUS, ERROR_UNKNOWN);

        switch (statusCode)
        {
            case ERROR_NO_CONNECTION:
                UiExtensions.showToast(context, MSG_ERROR_NO_CONNECTION);
                break;

            default:
                UiExtensions.showToast(context, MSG_ERROR_UNKNOWN);
        }
    }
}