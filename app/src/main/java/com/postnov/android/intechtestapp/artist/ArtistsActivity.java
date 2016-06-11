package com.postnov.android.intechtestapp.artist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.postnov.android.intechtestapp.R;
import com.postnov.android.intechtestapp.player.PlaybackService;

public class ArtistsActivity extends AppCompatActivity
{
    public static final String LAYOUT_TYPE = "layout_type";
    public static final int LAYOUT_LIST = 0;
    public static final int LAYOUT_GRID = 1;
    private static final String SHARED_PREF_NAME = "myPref";
    private int mDefaultLayoutType;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists);
        initToolbar();

        mSharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        mDefaultLayoutType = getLayoutType();

        if (savedInstanceState == null)
        {
            initFragment(ArtistsFragment.newInstance(mDefaultLayoutType));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        switch (itemId)
        {
            case R.id.action_change_layout:

                if (mDefaultLayoutType == LAYOUT_LIST)
                {
                    initFragment(ArtistsFragment.newInstance(LAYOUT_GRID));
                    setLayoutType(LAYOUT_GRID);

                    return true;
                }
                initFragment(ArtistsFragment.newInstance(LAYOUT_LIST));
                setLayoutType(LAYOUT_LIST);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.artists_toolbar);
        setSupportActionBar(toolbar);
    }

    private int getLayoutType()
    {
        return mSharedPreferences.getInt(LAYOUT_TYPE, LAYOUT_LIST);
    }

    private void setLayoutType(int type)
    {
        mSharedPreferences.edit().putInt(LAYOUT_TYPE, type).apply();
        mDefaultLayoutType = type;
    }
}
