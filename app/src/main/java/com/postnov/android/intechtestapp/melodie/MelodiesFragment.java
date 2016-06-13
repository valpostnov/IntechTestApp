package com.postnov.android.intechtestapp.melodie;

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
import android.widget.Button;
import android.widget.TextView;

import com.postnov.android.intechtestapp.Injection;
import com.postnov.android.intechtestapp.R;
import com.postnov.android.intechtestapp.melodie.interfaces.MelodiesPresenter;
import com.postnov.android.intechtestapp.melodie.interfaces.MelodiesView;
import com.postnov.android.intechtestapp.data.entity.Melodie;
import com.postnov.android.intechtestapp.player.PlayerActivity;
import com.postnov.android.intechtestapp.utils.NetworkManager;
import com.postnov.android.intechtestapp.utils.Utils;

import java.util.List;

public class MelodiesFragment extends Fragment implements MelodiesView, MelodiesAdapter.OnItemClickListener, View.OnClickListener
{
    public static final String EXTRA_MELODIE = "extra_melodie_object";
    private static final int SPAN_COUNT_DEF = 2;
    private static final int SPAN_COUNT_THREE = 3;
    private static final int START_COUNT_MELODIES = 20;
    private static final int NEXT_COUNT_MELODIES = 10;

    private MelodiesAdapter mMelodiesAdapter;
    private ProgressDialog mProgressDialog;
    private MelodiesPresenter mPresenter;
    private boolean isLandOrient;

    public MelodiesFragment() {}

    public static MelodiesFragment newInstance(int layoutType)
    {
        MelodiesFragment fragment = new MelodiesFragment();
        Bundle args = new Bundle();
        args.putInt(MelodiesActivity.LAYOUT_TYPE, layoutType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        isLandOrient = getContext().getResources().getBoolean(R.bool.landscape_orient);
        mPresenter = new MelodiesPresenterImpl(
                Injection.provideDataSource(),
                NetworkManager.getInstance(getContext()));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.bind(this);
        mPresenter.fetchMelodies(START_COUNT_MELODIES, 0);
    }

    @Override
    public void onPause()
    {
        mPresenter.unsubscribe();
        mPresenter.unbind();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        View rootView = inflater.inflate(R.layout.fragment_melodies, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void showMelodies(List<Melodie> melodies)
    {
        mMelodiesAdapter.swapList(melodies);
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
        Melodie melodie = mMelodiesAdapter.getList().get(position);

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

                int loadedMelodies = mMelodiesAdapter.getItemCount();
                mPresenter.fetchMelodies(NEXT_COUNT_MELODIES, loadedMelodies + 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(View view)
    {
        View emptyView = view.findViewById(R.id.melodies_emptyview);
        Button refreshButton = (Button) view.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(this);

        int layoutType = getArguments().getInt(MelodiesActivity.LAYOUT_TYPE);
        RecyclerView.LayoutManager layoutManager;

        if (layoutType == MelodiesActivity.LAYOUT_GRID)
        {
            layoutManager = new GridLayoutManager(getContext(), isLandOrient ? SPAN_COUNT_THREE : SPAN_COUNT_DEF);
            mMelodiesAdapter = new MelodiesAdapter(getContext(), R.layout.item_grid_melodies);
        }
        else
        {
            layoutManager = new LinearLayoutManager(getContext());
            mMelodiesAdapter = new MelodiesAdapter(getContext(), R.layout.item_list_melodies);
        }

        mMelodiesAdapter.setOnItemClickListener(this);
        mMelodiesAdapter.setEmptyView(emptyView);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.melodies_recyclerview);
        recyclerView.setAdapter(mMelodiesAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getString(R.string.loading_artists));
    }

    @Override
    public void onClick(View v)
    {
        mPresenter.fetchMelodies(START_COUNT_MELODIES, 0);
    }
}
