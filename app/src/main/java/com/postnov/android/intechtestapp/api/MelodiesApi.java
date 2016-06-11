package com.postnov.android.intechtestapp.api;

import com.postnov.android.intechtestapp.data.entity.Response;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by platon on 10.06.2016.
 */
public interface MelodiesApi
{
    @GET("melodies")
    Observable<Response> getMelodies(@Query("limit") int limit, @Query("from") int from);
}
