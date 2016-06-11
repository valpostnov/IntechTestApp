package com.postnov.android.intechtestapp.data.source;

import com.postnov.android.intechtestapp.data.entity.Melodie;
import com.postnov.android.intechtestapp.data.entity.Response;

import java.util.List;

import rx.Observable;

/**
 * Created by platon on 10.06.2016.
 */
public interface DataSource
{
    Observable<Response> getMelodies(int limit, int from);
}
