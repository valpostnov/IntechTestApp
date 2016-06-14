package com.postnov.android.intechtestapp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by platon on 11.06.2016.
 */
public class UiExtensions
{
    public static void showToast(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message, int length)
    {
        Toast.makeText(context, message, length).show();
    }
}
