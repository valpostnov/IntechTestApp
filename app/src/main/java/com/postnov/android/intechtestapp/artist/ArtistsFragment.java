package com.postnov.android.intechtestapp.artist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.postnov.android.intechtestapp.Injection;
import com.postnov.android.intechtestapp.R;
import com.postnov.android.intechtestapp.artist.interfaces.ArtistPresenter;
import com.postnov.android.intechtestapp.artist.interfaces.ArtistView;
import com.postnov.android.intechtestapp.data.entity.Melodie;
import com.postnov.android.intechtestapp.player.PlayerActivity;
import com.postnov.android.intechtestapp.utils.Utils;

import java.util.List;

public class ArtistsFragment extends Fragment implements ArtistView, ArtistsAdapter.OnItemClickListener
{
    public static final String EXTRA_MELODIE = "extra_melodie_object";
    private static final int SPAN_COUNT_DEF = 2;
    private static final int SPAN_COUNT_THREE = 3;

    private ArtistsAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private ArtistPresenter mPresenter;
    private boolean isLandOrient;

    public ArtistsFragment() {}

    public static ArtistsFragment newInstance(int layoutType)
    {
        ArtistsFragment fragment = new ArtistsFragment();
        Bundle args = new Bundle();
        args.putInt(ArtistsActivity.LAYOUT_TYPE, layoutType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        isLandOrient = getContext().getResources().getBoolean(R.bool.landscape_orient);
        mPresenter = new ArtistPresenterImpl(Injection.provideDataSource());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.bind(this);
        mPresenter.fetchArtists(20, 0);
    }

    @Override
    public void onPause()
    {
        mPresenter.unsubscribe();
        mPresenter.unbind();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_list_artist, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void showArtists(List<Melodie> melodies)
    {
        mAdapter.swapList(melodies);
    }

    @Override
    public void showProgressDialog()
    {
        mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog()
    {
        mProgressDialog.dismiss();
    }

    @Override
    public void showError(String error)
    {
        Utils.showToast(getContext(), error);
    }

    @Override
    public void onItemClick(View view, int position)
    {
        Melodie melodie = mAdapter.getList().get(position);

        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra(EXTRA_MELODIE, melodie);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_load_more:

                int loadedMelodies = mAdapter.getItemCount();
                int nextTen = 10;

                mPresenter.fetchArtists(nextTen, loadedMelodies + 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(View view)
    {
        TextView emptyView = (TextView) view.findViewById(R.id.artists_emptyview);

        int layoutType = getArguments().getInt(ArtistsActivity.LAYOUT_TYPE);
        RecyclerView.LayoutManager layoutManager;

        if (layoutType == ArtistsActivity.LAYOUT_GRID)
        {
            layoutManager = new GridLayoutManager(getContext(), isLandOrient ? SPAN_COUNT_THREE : SPAN_COUNT_DEF);
            mAdapter = new ArtistsAdapter(getContext(), R.layout.item_grid_artist);
        }
        else
        {
            layoutManager = new LinearLayoutManager(getContext());
            mAdapter = new ArtistsAdapter(getContext(), R.layout.item_list_artist);
        }

        mAdapter.setOnItemClickListener(this);
        mAdapter.setEmptyView(emptyView);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.artists_recyclerview);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getString(R.string.loading_artists));
    }
}
