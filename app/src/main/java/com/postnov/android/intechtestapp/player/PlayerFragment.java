package com.postnov.android.intechtestapp.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.postnov.android.intechtestapp.R;
import com.postnov.android.intechtestapp.data.entity.Melodie;
import com.postnov.android.intechtestapp.melodie.MelodiesFragment;
import com.postnov.android.intechtestapp.utils.Utils;

import static com.postnov.android.intechtestapp.player.PlaybackService.EXTENDED_DATA_STATUS;
import static com.postnov.android.intechtestapp.player.PlaybackService.PAUSE;
import static com.postnov.android.intechtestapp.player.PlaybackService.PLAY;
import static com.postnov.android.intechtestapp.player.PlaybackService.STOP;
import static com.postnov.android.intechtestapp.utils.Const.ERROR_NO_CONNECTION;
import static com.postnov.android.intechtestapp.utils.Const.ERROR_UNKNOWN;
import static com.postnov.android.intechtestapp.utils.Const.MSG_ERROR_NO_CONNECTION;
import static com.postnov.android.intechtestapp.utils.Const.MSG_ERROR_UNKNOWN;

public class PlayerFragment extends Fragment implements View.OnClickListener
{
    public static final String EXTRA_DEMO_URL = "extra_demo_url";

    private enum States
    {
         PAUSE, PLAY, STOP
    }

    private PlaybackStateReceiver mPlaybackStateReceiver;
    private FloatingActionButton playPauseButton;
    private Context mContext;
    private States mState;
    private Melodie mMelodie;

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
        mMelodie = getArguments().getParcelable(MelodiesFragment.EXTRA_MELODIE);
        setState(States.PLAY);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        registerLBReceiver();
    }

    @Override
    public void onDestroyView()
    {
        unregisterLBReceiver();
        super.onDestroyView();
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
                selectAction();
                break;
        }
    }

    private void initViews(View view)
    {
        ImageView albumPic = (ImageView) view.findViewById(R.id.player_album_pic);
        TextView artist = (TextView) view.findViewById(R.id.player_artist);
        TextView melodie = (TextView) view.findViewById(R.id.player_melodie_title);

        playPauseButton = (FloatingActionButton) view.findViewById(R.id.play_button);
        playPauseButton.setOnClickListener(this);
        setPlayButtonIcon();

        Button stopPlayback = (Button) view.findViewById(R.id.stop_button);
        stopPlayback.setOnClickListener(this);

        Glide.with(getContext()).load(mMelodie.getPicUrl()).into(albumPic);
        artist.setText(mMelodie.getArtist());
        melodie.setText(mMelodie.getTitle());
    }

    private void play()
    {
        Intent intent = new Intent(mContext, PlaybackService.class);
        intent.setAction(PlaybackService.ACTION_PLAY);
        intent.putExtra(EXTRA_DEMO_URL, mMelodie.getDemoUrl());
        mContext.startService(intent);
    }

    private void pause()
    {
        Intent intent = new Intent(mContext, PlaybackService.class);
        intent.setAction(PlaybackService.ACTION_PAUSE);
        mContext.startService(intent);
    }

    private void stop()
    {
        Intent intent = new Intent(mContext, PlaybackService.class);
        intent.setAction(PlaybackService.ACTION_STOP);
        mContext.startService(intent);
    }

    private void selectAction()
    {
        switch (mState)
        {
            case PLAY:
                pause();
                break;

            case PAUSE:
                play();
                break;

            case STOP:
                play();
                break;
        }
    }

    private void setPlayButtonIcon()
    {
        switch (mState)
        {
            case PLAY:
                playPauseButton.setImageResource(R.drawable.ic_pause);
                break;

            default:
                playPauseButton.setImageResource(R.drawable.ic_play);
        }
    }

    private void setState(States state)
    {
        mState = state;
    }

    private void registerLBReceiver()
    {
        IntentFilter statusFilter = new IntentFilter(PlaybackService.BROADCAST_ACTION);
        statusFilter.addCategory(Intent.CATEGORY_DEFAULT);
        mPlaybackStateReceiver = new PlaybackStateReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mPlaybackStateReceiver, statusFilter);
    }

    private void unregisterLBReceiver()
    {
        if (mPlaybackStateReceiver != null)
        {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mPlaybackStateReceiver);
            mPlaybackStateReceiver = null;
        }
    }

    private class PlaybackStateReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            int statusCode = intent.getIntExtra(EXTENDED_DATA_STATUS, ERROR_UNKNOWN);

            switch (statusCode)
            {
                case PLAY:
                    setState(States.PLAY);
                    setPlayButtonIcon();
                    break;

                case PAUSE:
                    setState(States.PAUSE);
                    setPlayButtonIcon();
                    break;

                case STOP:
                    setState(States.STOP);
                    setPlayButtonIcon();
                    break;

                case ERROR_NO_CONNECTION:
                    Utils.showToast(context, MSG_ERROR_NO_CONNECTION);
                    setState(States.STOP);
                    setPlayButtonIcon();
                    break;

                default:
                    Utils.showToast(context, MSG_ERROR_UNKNOWN);
                    setState(States.STOP);
                    setPlayButtonIcon();
            }
        }
    }
}
