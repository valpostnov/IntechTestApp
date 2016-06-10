package com.postnov.android.intechtestapp.data.source;

import com.postnov.android.intechtestapp.api.MelodiesApi;
import com.postnov.android.intechtestapp.data.entity.Response;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by platon on 10.06.2016.
 */
public class DataSourceImpl implements DataSource
{
    private MelodiesApi mApi;
    private static final String ENDPOINT = "url";

    public DataSourceImpl()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mApi = retrofit.create(MelodiesApi.class);
    }

    @Override
    public Observable<Response> getMelodies(String limit, String from)
    {
        return mApi.getMelodies(limit, from);
    }
}
