package com.postnov.android.intechtestapp.player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

public class PlaybackService extends Service implements MediaPlayer.OnPreparedListener
{
    public static final String ACTION_PLAY = "com.postnov.action.PLAY";
    public static final String ACTION_PAUSE = "com.postnov.action.PAUSE";
    public static final String ACTION_STOP = "com.postnov.action.STOP";

    private MediaPlayer mMediaPlayer;
    private int mCurrentPosition;

    public PlaybackService() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        String url = intent.getStringExtra(PlayerFragment.EXTRA_DEMO_URL);
        String action = intent.getAction();

        switch (action)
        {
            case ACTION_PLAY:
                start(url);
                break;

            case ACTION_PAUSE:
                pause();
                break;

            case ACTION_STOP:
                stop();
                break;
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onDestroy()
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        super.onDestroy();
    }

    private void initMediaPlayer(String url)
    {
        try
        {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        mp.start();
    }

    private void start(String url)
    {
        if (mCurrentPosition > 0)
        {
            mMediaPlayer.seekTo(mCurrentPosition);
            mMediaPlayer.start();
            mCurrentPosition = 0;
        }
        else
        {
            initMediaPlayer(url);
        }
    }

    private void pause()
    {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
        {
            mMediaPlayer.pause();
            mCurrentPosition = mMediaPlayer.getCurrentPosition();
        }
    }

    private void stop()
    {
        if (mMediaPlayer != null)
        {
            mMediaPlayer.stop();
            mCurrentPosition = 0;
        }
    }
}
