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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.postnov.android.intechtestapp.R;
import com.postnov.android.intechtestapp.data.entity.Melodie;
import com.postnov.android.intechtestapp.melodie.MelodiesFragment;

public class PlayerFragment extends Fragment implements View.OnClickListener
{
    public static final String EXTRA_DEMO_URL = "extra_demo_url";
    private Context mContext;
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
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.stop_button:
                stop();
                break;

            case R.id.pause_button:
                pause();
                break;

            case R.id.play_button:
                play();
                break;
        }
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

    private void initViews(View view)
    {
        ImageView album = (ImageView) view.findViewById(R.id.player_album_pic);
        TextView artist = (TextView) view.findViewById(R.id.player_artist);
        TextView melodie = (TextView) view.findViewById(R.id.player_melodie_title);

        ImageButton stopBtn = (ImageButton) view.findViewById(R.id.stop_button);
        ImageButton pauseBtn = (ImageButton) view.findViewById(R.id.pause_button);
        FloatingActionButton playBtn = (FloatingActionButton) view.findViewById(R.id.play_button);

        playBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);

        Glide.with(getContext()).load(mMelodie.getPicUrl()).into(album);
        artist.setText(mMelodie.getArtist());
        melodie.setText(mMelodie.getTitle());
    }
}
