package com.postnov.android.intechtestapp.melodie;

import android.content.Context;
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

public class MelodiesActivity extends AppCompatActivity
{
    public static final String LIST_LAYOUT_TYPE = "list_type";
    private boolean isListLayoutType;
    private static final String SHARED_PREF_NAME = "myPref";
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_melodies);
        initToolbar();

        mSharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        isListLayoutType = getLayoutType();

        if (savedInstanceState == null)
        {
            initFragment(MelodiesFragment.newInstance(isListLayoutType));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        int gridIcon = R.drawable.ic_view_module;
        int listIcon = R.drawable.ic_view_list;

        MenuItem item = menu.findItem(R.id.action_change_layout);
        item.setIcon(isListLayoutType ? gridIcon : listIcon);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        switch (itemId)
        {
            case R.id.action_change_layout:

                initFragment(MelodiesFragment.newInstance(!isListLayoutType));
                setLayoutType(!isListLayoutType);

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
        toolbar.setTitle(R.string.main_title);
        setSupportActionBar(toolbar);
    }

    private boolean getLayoutType()
    {
        return mSharedPreferences.getBoolean(LIST_LAYOUT_TYPE, true);
    }

    private void setLayoutType(boolean type)
    {
        mSharedPreferences.edit().putBoolean(LIST_LAYOUT_TYPE, type).apply();
        isListLayoutType = type;
    }
}
