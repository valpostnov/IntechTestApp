package com.postnov.android.intechtestapp.data;

import com.postnov.android.intechtestapp.Injection;
import com.postnov.android.intechtestapp.data.entity.Melodie;
import com.postnov.android.intechtestapp.data.entity.Response;
import com.postnov.android.intechtestapp.data.source.DataSource;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import rx.observers.TestSubscriber;

public class DataSourceUnitTest
{
    private DataSource mDataSource;
    private int mLimit;
    private int mFrom;

    @Before
    public void setupDataSource()
    {
        mDataSource = Injection.provideDataSource();
        mLimit = 20;
        mFrom = 0;
    }

    @Test
    public void testGetMelodies() throws Exception
    {
        TestSubscriber<Response> testSubscriber = new TestSubscriber<>();
        mDataSource.getMelodies(mLimit, mFrom).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
    }
}