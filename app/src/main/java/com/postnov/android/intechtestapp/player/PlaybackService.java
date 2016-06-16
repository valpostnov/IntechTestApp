package com.postnov.android.intechtestapp.player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.postnov.android.intechtestapp.MelodiesApp;
import com.postnov.android.intechtestapp.bus.RxBus;
import com.postnov.android.intechtestapp.bus.events.ErrorEvent;
import com.postnov.android.intechtestapp.bus.events.PlaybackEvent;
import com.postnov.android.intechtestapp.utils.NetworkManager;

import java.io.IOException;

import static com.postnov.android.intechtestapp.bus.events.PlaybackEvent.*;
import static com.postnov.android.intechtestapp.utils.Const.MSG_ERROR_NO_CONNECTION;
import static com.postnov.android.intechtestapp.utils.Const.MSG_ERROR_UNKNOWN;

public class PlaybackService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener {

    private static final String TAG = "PlaybackService";

    public static final String ACTION_PLAY = "com.postnov.action.PLAY";
    public static final String ACTION_PAUSE = "com.postnov.action.PAUSE";
    public static final String ACTION_STOP = "com.postnov.action.STOP";

    private MediaPlayer mMediaPlayer;
    private NetworkManager mNetworkManager;
    private RxBus mRxBus;
    private int mCurrentPosition;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mNetworkManager = NetworkManager.getInstance(this);
        mRxBus = MelodiesApp.getEventBus();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (mNetworkManager.networkIsAvailable())
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

        mRxBus.post(new ErrorEvent(MSG_ERROR_NO_CONNECTION));
        stopSelf(startId);

        return START_NOT_STICKY;
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
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
        }
        catch (IOException e)
        {
            mRxBus.post(new ErrorEvent(MSG_ERROR_UNKNOWN));
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onPrepared(MediaPlayer player)
    {
        player.start();
    }

    @Override
    public boolean onError(MediaPlayer player, int what, int extra)
    {
        mRxBus.post(new ErrorEvent(MSG_ERROR_UNKNOWN));
        stopSelf();
        return true;
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
            if (mMediaPlayer != null) mMediaPlayer.reset();
            initMediaPlayer(url);
        }

        mRxBus.post(new PlaybackEvent(PLAY));
    }

    private void pause()
    {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
        {
            mMediaPlayer.pause();
            mCurrentPosition = mMediaPlayer.getCurrentPosition();
            mRxBus.post(new PlaybackEvent(PAUSE));
        }
    }

    private void stop()
    {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
        {
            mMediaPlayer.stop();
            mCurrentPosition = 0;
            mRxBus.post(new PlaybackEvent(STOP));
        }
    }
}
