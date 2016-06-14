package com.postnov.android.intechtestapp.player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.postnov.android.intechtestapp.utils.Const;
import com.postnov.android.intechtestapp.utils.NetworkManager;

import java.io.IOException;

import static com.postnov.android.intechtestapp.utils.Const.ERROR_NO_CONNECTION;
import static com.postnov.android.intechtestapp.utils.Const.ERROR_UNKNOWN;

public class PlaybackService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener
{
    public static final String ACTION_PLAY = "com.postnov.action.PLAY";
    public static final String ACTION_PAUSE = "com.postnov.action.PAUSE";
    public static final String ACTION_STOP = "com.postnov.action.STOP";

    public static final int PLAY = 99;
    public static final int PAUSE = 98;
    public static final int STOP = 97;

    public static final String BROADCAST_ACTION = "com.postnov.action.BROADCAST";
    public static final String EXTENDED_DATA_STATUS = "com.postnov.action.STATUS";
    private static final String TAG = "PlaybackService";

    private MediaPlayer mMediaPlayer;
    private NetworkManager mNetworkManager;
    private int mCurrentPosition;

    public PlaybackService() {}

    @Override
    public void onCreate()
    {
        super.onCreate();
        mNetworkManager = NetworkManager.getInstance(this);
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

        sendStatus(ERROR_NO_CONNECTION);
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
            sendStatus(ERROR_UNKNOWN);
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onPrepared(MediaPlayer player)
    {
        player.start();
        sendStatus(PLAY);
    }

    @Override
    public boolean onError(MediaPlayer player, int what, int extra)
    {
        Log.e(TAG, "error: " + extra);
        sendStatus(extra);
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
            sendStatus(PLAY);
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
            sendStatus(PAUSE);
        }
    }

    private void stop()
    {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
        {
            mMediaPlayer.stop();
            mCurrentPosition = 0;
            sendStatus(STOP);
        }
    }

    private void sendStatus(int status)
    {
        Intent localIntent = new Intent(BROADCAST_ACTION);
        localIntent.putExtra(EXTENDED_DATA_STATUS, status);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
