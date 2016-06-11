package com.postnov.android.intechtestapp.data.source;

import com.postnov.android.intechtestapp.api.MelodiesApi;
import com.postnov.android.intechtestapp.data.entity.Melodie;
import com.postnov.android.intechtestapp.data.entity.Response;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by platon on 10.06.2016.
 */
public class DataSourceImpl implements DataSource
{
    private static DataSourceImpl sInstance;

    private MelodiesApi mApi;
    private Response mResponseCache;
    private static final int DEFAULT_LIMIT = 20;
    private static final String ENDPOINT = "url";

    public static DataSourceImpl getInstance()
    {
        if (sInstance == null)
        {
            sInstance = new DataSourceImpl();
        }

        return sInstance;
    }

    private DataSourceImpl()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mApi = retrofit.create(MelodiesApi.class);;
    }

    @Override
    public Observable<Response> getMelodies(int limit, int from)
    {
        if (limit == DEFAULT_LIMIT && mResponseCache != null)
        {
            return Observable.just(mResponseCache);
        }
        return fetchAndSave(limit, from);
    }

    private Observable<Response> fetchAndSave(int limit, int from)
    {
        return mApi.getMelodies(limit, from).doOnNext(new Action1<Response>()
        {
            @Override
            public void call(Response response)
            {
                if (mResponseCache != null)
                {
                    mResponseCache.getMelodies().addAll(response.getMelodies());
                }
                else
                {
                    mResponseCache = response;
                }
            }
        });
    }
}
