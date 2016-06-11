package com.postnov.android.intechtestapp.player;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.postnov.android.intechtestapp.R;
import com.postnov.android.intechtestapp.artist.ArtistsFragment;
import com.postnov.android.intechtestapp.data.entity.Melodie;

public class PlayerFragment extends Fragment implements View.OnClickListener
{
    public static final String EXTRA_DEMO_URL = "extra_demo_url";

    private enum PlayerStates
    {
         PAUSE, PLAY
    }

    private FloatingActionButton playPauseButton;
    private Context mContext;
    private PlayerStates mState;
    private Melodie mMelodie;

    public PlayerFragment() {}

    public static PlayerFragment newInstance(Melodie melodie)
    {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putParcelable(ArtistsFragment.EXTRA_MELODIE, melodie);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mContext = getContext();
        mMelodie = getArguments().getParcelable(ArtistsFragment.EXTRA_MELODIE);
        selectAction(PlayerStates.PAUSE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState)
    {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onClick(View v)
    {
        selectAction(mState);
        setButtonIcon(mState);
    }

    private void initViews(View view)
    {
        ImageView albumPic = (ImageView) view.findViewById(R.id.player_album_pic);
        TextView artistTextView = (TextView) view.findViewById(R.id.player_artist);
        TextView melodieTextView = (TextView) view.findViewById(R.id.player_melodie_title);

        playPauseButton = (FloatingActionButton) view.findViewById(R.id.play_button);
        setButtonIcon(mState);
        playPauseButton.setOnClickListener(this);

        Glide.with(getContext()).load(mMelodie.getPicUrl()).into(albumPic);
        artistTextView.setText(mMelodie.getArtist());
        melodieTextView.setText(mMelodie.getTitle());
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

    private void selectAction(PlayerStates state)
    {
        switch (state)
        {
            case PAUSE:
                play();
                mState = PlayerStates.PLAY;
                break;

            case PLAY:
                pause();
                mState = PlayerStates.PAUSE;
                break;
        }
    }

    private void setButtonIcon(PlayerStates state)
    {
        switch (state)
        {
            case PAUSE:
                playPauseButton.setImageResource(R.drawable.ic_play_arrow);
                break;

            case PLAY:
                playPauseButton.setImageResource(R.drawable.ic_pause);
                break;
        }
    }
}
