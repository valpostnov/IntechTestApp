package com.postnov.android.intechtestapp.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.postnov.android.intechtestapp.MelodiesApp;
import com.postnov.android.intechtestapp.R;
import com.postnov.android.intechtestapp.bus.RxBus;
import com.postnov.android.intechtestapp.bus.events.ErrorEvent;
import com.postnov.android.intechtestapp.bus.events.PlaybackEvent;
import com.postnov.android.intechtestapp.data.entity.Melodie;
import com.postnov.android.intechtestapp.melodie.MelodiesFragment;
import com.postnov.android.intechtestapp.utils.UiExtensions;

import rx.Subscription;
import rx.functions.Action1;

import static com.postnov.android.intechtestapp.bus.events.PlaybackEvent.PLAY;
import static com.postnov.android.intechtestapp.bus.events.PlaybackEvent.STOP;
import static com.postnov.android.intechtestapp.player.PlaybackService.ACTION_PAUSE;
import static com.postnov.android.intechtestapp.player.PlaybackService.ACTION_PLAY;
import static com.postnov.android.intechtestapp.player.PlaybackService.ACTION_STOP;

public class PlayerFragment extends Fragment implements View.OnClickListener
{
    public static final String EXTRA_DEMO_URL = "extra_demo_url";

    private Context mContext;
    private FloatingActionButton mPlayBtn;
    private Melodie mMelodie;
    private RxBus mRxBus;
    private Subscription mRxBusSubscription;
    private int mState = STOP;

    public PlayerFragment() {}

    public static PlayerFragment newInstance(Melodie melodie)
    {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putParcelable(MelodiesFragment.EXTRA_MELODIE, melodie);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mContext = getContext();
        mRxBus = MelodiesApp.getEventBus();
        mMelodie = getArguments().getParcelable(MelodiesFragment.EXTRA_MELODIE);
        play();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState)
    {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mRxBusSubscription = mRxBus.toObservable().subscribe(new Action1<Object>()
        {
            @Override
            public void call(Object event)
            {
                if (event instanceof ErrorEvent)
                {
                    UiExtensions.showToast(mContext, ((ErrorEvent) event).getError());
                }
                else if (event instanceof PlaybackEvent)
                {
                    mState = ((PlaybackEvent) event).getState();
                    setPlayButtonIcon(mState);
                }
            }
        });
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mRxBusSubscription.unsubscribe();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.stop_button:
                stop();
                break;

            case R.id.play_button:

                if (mState == PLAY) pause();
                else play();

                break;
        }
    }

    private void play()
    {
        Intent intent = new Intent(mContext, PlaybackService.class);
        intent.putExtra(EXTRA_DEMO_URL, mMelodie.getDemoUrl());
        intent.setAction(ACTION_PLAY);
        mContext.startService(intent);
    }

    private void pause()
    {
        Intent intent = new Intent(mContext, PlaybackService.class);
        intent.setAction(ACTION_PAUSE);
        mContext.startService(intent);
    }

    private void stop()
    {
        Intent intent = new Intent(mContext, PlaybackService.class);
        intent.setAction(ACTION_STOP);
        mContext.startService(intent);
    }

    private void initViews(View view)
    {
        ImageView album = (ImageView) view.findViewById(R.id.player_album_pic);
        TextView artist = (TextView) view.findViewById(R.id.player_artist);
        TextView melodie = (TextView) view.findViewById(R.id.player_melodie_title);

        Button stopBtn = (Button) view.findViewById(R.id.stop_button);
        mPlayBtn = (FloatingActionButton) view.findViewById(R.id.play_button);
        setPlayButtonIcon(mState);

        mPlayBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);

        Glide.with(getContext()).load(mMelodie.getPicUrl()).into(album);
        artist.setText(mMelodie.getArtist());
        melodie.setText(mMelodie.getTitle());
    }

    private void setPlayButtonIcon(int state)
    {
        mPlayBtn.setImageResource(state == PLAY ? R.drawable.ic_pause : R.drawable.ic_play);
    }
}
