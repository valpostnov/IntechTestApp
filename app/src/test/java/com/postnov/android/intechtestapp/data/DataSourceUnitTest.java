package com.postnov.android.intechtestapp.data;

import com.postnov.android.intechtestapp.Injection;
import com.postnov.android.intechtestapp.data.entity.Response;
import com.postnov.android.intechtestapp.data.source.DataSource;

import org.junit.Before;
import org.junit.Test;

import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;

public class DataSourceUnitTest
{
    private DataSource mDataSource;
    private String mLimit;
    private String mFrom;

    @Before
    public void setupDataSource()
    {
        mDataSource = Injection.provideDataSource();
        mLimit = "20";
        mFrom = "1";
    }

    @Test
    public void testGetMelodies() throws Exception
    {
        TestSubscriber<Response> testSubscriber = new TestSubscriber<>();
        mDataSource.getMelodies(mLimit, mFrom).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
    }
}