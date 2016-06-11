package com.postnov.android.intechtestapp;

import com.postnov.android.intechtestapp.data.source.DataSource;
import com.postnov.android.intechtestapp.data.source.DataSourceImpl;

/**
 * Created by platon on 10.06.2016.
 */
public class Injection
{
    public static DataSource provideDataSource()
    {
        return DataSourceImpl.getInstance();
    }
}
