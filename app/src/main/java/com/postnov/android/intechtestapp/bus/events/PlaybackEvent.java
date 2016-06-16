package com.postnov.android.intechtestapp.bus.events;

/**
 * Created by platon on 16.06.2016.
 */
public class PlaybackEvent
{
    public static final int PLAY = 1000;
    public static final int PAUSE = 1001;
    public static final int STOP = 1002;

    private int mState;

    public PlaybackEvent(int state)
    {
        mState = state;
    }

    public int getState()
    {
        return mState;
    }

    @Override
    public String toString()
    {
        switch (mState)
        {
            case PAUSE:
                return "pause";

            case STOP:
                return "stop";

            default:
                return "play";
        }
    }
}
