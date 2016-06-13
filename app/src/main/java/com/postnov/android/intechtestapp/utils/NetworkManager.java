package com.postnov.android.intechtestapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager
{
    private static NetworkManager sInstance;
    private Context mContext;

    public static NetworkManager getInstance(Context context)
    {
        if (sInstance == null)
        {
            sInstance = new NetworkManager(context);
        }

        return sInstance;
    }

    private NetworkManager(Context context)
    {
        mContext = context;
    }

    public boolean networkIsAvailable()
    {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        return (activeInfo != null && activeInfo.isConnected());
    }
}
