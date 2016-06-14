package com.postnov.android.intechtestapp.melodie;

import com.postnov.android.intechtestapp.data.entity.Response;
import com.postnov.android.intechtestapp.data.source.DataSource;
import com.postnov.android.intechtestapp.melodie.interfaces.MelodiesPresenter;
import com.postnov.android.intechtestapp.melodie.interfaces.MelodiesView;
import com.postnov.android.intechtestapp.utils.Const;
import com.postnov.android.intechtestapp.utils.NetworkManager;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by platon on 10.06.2016.
 */
public class MelodiesPresenterImpl implements MelodiesPresenter
{
    private MelodiesView mView;
    private DataSource mDataSource;
    private CompositeSubscription mSubscriptions;
    private NetworkManager mNetworkManager;

    public MelodiesPresenterImpl(DataSource dataSource, NetworkManager networkManager)
    {
        mDataSource = dataSource;
        mSubscriptions = new CompositeSubscription();
        mNetworkManager = networkManager;
    }

    @Override
    public void fetchMelodies(int limit, int from)
    {
        if (mNetworkManager.networkIsAvailable()) 
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
                        { mView.hideProgressDialog(); }

                        @Override
                        public void onError(Throwable e) 
                        {
                            mView.hideProgressDialog();

                            mView.showError(e.getMessage());
                        }

                        @Override
                        public void onNext(Response response)
                        { mView.showMelodies(response.getMelodies()); }
                        
                    }));
        }
        else 
        {
            mView.showError(Const.MSG_ERROR_NO_CONNECTION);
            mView.showMelodies(null);
        }
    }

    @Override
    public void bind(MelodiesView view)
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
