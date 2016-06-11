package com.postnov.android.intechtestapp.artist;

import com.postnov.android.intechtestapp.artist.interfaces.ArtistPresenter;
import com.postnov.android.intechtestapp.artist.interfaces.ArtistView;
import com.postnov.android.intechtestapp.data.entity.Response;
import com.postnov.android.intechtestapp.data.source.DataSource;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by platon on 10.06.2016.
 */
public class ArtistPresenterImpl implements ArtistPresenter
{
    private ArtistView mView;
    private DataSource mDataSource;
    private CompositeSubscription mSubscriptions;

    public ArtistPresenterImpl(DataSource dataSource)
    {
        mDataSource = dataSource;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void fetchArtists(int limit, int from)
    {
        mView.showProgressDialog();
        mSubscriptions.add(mDataSource
                .getMelodies(limit, from)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response>()
                {
                    @Override
                    public void onCompleted()
                    {
                        mView.hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        mView.hideProgressDialog();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Response response)
                    {
                        mView.showArtists(response.getMelodies());
                    }
                }));
    }

    @Override
    public void bind(ArtistView view)
    {
        mView = view;
    }

    @Override
    public void unbind()
    {
        mView = null;
    }

    @Override
    public void unsubscribe()
    {
        mSubscriptions.clear();
    }
}
