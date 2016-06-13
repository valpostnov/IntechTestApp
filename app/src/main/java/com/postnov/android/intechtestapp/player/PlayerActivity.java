package com.postnov.android.intechtestapp.player;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.postnov.android.intechtestapp.R;
import com.postnov.android.intechtestapp.data.entity.Melodie;
import com.postnov.android.intechtestapp.melodie.MelodiesFragment;

public class PlayerActivity extends AppCompatActivity
{
    private PlaybackStateReceiver mPlaybackStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initToolbar();
        initLBReceiver();

        Melodie melodie = getIntent().getParcelableExtra(MelodiesFragment.EXTRA_MELODIE);

        if (savedInstanceState == null)
        {
            initFragment(PlayerFragment.newInstance(melodie));
        }
    }

    @Override
    protected void onDestroy()
    {
        if (mPlaybackStateReceiver != null)
        {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mPlaybackStateReceiver);
            mPlaybackStateReceiver = null;
        }
        super.onDestroy();
    }

    private void initFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.player_container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.player_toolbar);
        toolbar.setTitle(R.string.player_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, PlaybackService.class);
        stopService(intent);
        super.onBackPressed();
    }

    private void initLBReceiver()
    {
        IntentFilter statusFilter = new IntentFilter(PlaybackService.BROADCAST_ACTION);
        statusFilter.addCategory(Intent.CATEGORY_DEFAULT);
        mPlaybackStateReceiver = new PlaybackStateReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mPlaybackStateReceiver, statusFilter);
    }
}
