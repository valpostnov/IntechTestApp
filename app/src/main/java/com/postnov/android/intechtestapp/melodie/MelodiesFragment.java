package com.postnov.android.intechtestapp.melodie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.postnov.android.intechtestapp.Injection;
import com.postnov.android.intechtestapp.R;
import com.postnov.android.intechtestapp.data.entity.Melodie;
import com.postnov.android.intechtestapp.melodie.interfaces.MelodiesPresenter;
import com.postnov.android.intechtestapp.melodie.interfaces.MelodiesView;
import com.postnov.android.intechtestapp.player.PlayerActivity;
import com.postnov.android.intechtestapp.utils.NetworkManager;
import com.postnov.android.intechtestapp.utils.UiExtensions;

import java.util.List;

import static com.postnov.android.intechtestapp.utils.Const.DEFAULT_LIMIT_MELODIES;
import static com.postnov.android.intechtestapp.utils.Const.NEXT_COUNT_MELODIES;

public class MelodiesFragment extends Fragment implements MelodiesView,
        MelodiesAdapter.OnItemClickListener, View.OnClickListener,
        MelodiesAdapter.OnEndlessListener
{
    public static final String EXTRA_MELODIE = "extra_melodie_object";
    private static final int SPAN_COUNT_DEF = 2;
    private static final int SPAN_COUNT_THREE = 3;

    private MelodiesAdapter mMelodiesAdapter;
    private ProgressDialog mProgressDialog;
    private MelodiesPresenter mPresenter;
    private boolean isLandOrient;

    public MelodiesFragment() {}

    public static MelodiesFragment newInstance(boolean isList)
    {
        MelodiesFragment fragment = new MelodiesFragment();
        Bundle args = new Bundle();
        args.putBoolean(MelodiesActivity.LIST_LAYOUT_TYPE, isList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        isLandOrient = getContext().getResources().getBoolean(R.bool.landscape_orient);
        NetworkManager networkManager = NetworkManager.getInstance(getContext().getApplicationContext());
        mPresenter = new MelodiesPresenterImpl(Injection.provideDataSource(), networkManager);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.bind(this);
        mPresenter.fetchMelodies(DEFAULT_LIMIT_MELODIES, 0);
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
        UiExtensions.showToast(getContext(), error);
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
    public void onClick(View v)
    {
        mPresenter.fetchMelodies(DEFAULT_LIMIT_MELODIES, 0);
    }

    @Override
    public void loadMore(int position)
    {
        mPresenter.fetchMelodies(NEXT_COUNT_MELODIES, position + 1);
    }

    private void initViews(View view)
    {
        View emptyView = view.findViewById(R.id.melodies_emptyview);
        Button refreshButton = (Button) view.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(this);

        boolean isListLayoutType = getArguments().getBoolean(MelodiesActivity.LIST_LAYOUT_TYPE);
        RecyclerView.LayoutManager layoutManager;

        if (isListLayoutType)
        {
            layoutManager = new LinearLayoutManager(getContext());
            mMelodiesAdapter = new MelodiesAdapter(getContext(), R.layout.item_list_melodies);
        }
        else
        {
            layoutManager = new GridLayoutManager(getContext(), isLandOrient ? SPAN_COUNT_THREE : SPAN_COUNT_DEF);
            mMelodiesAdapter = new MelodiesAdapter(getContext(), R.layout.item_grid_melodies);
        }

        mMelodiesAdapter.setOnItemClickListener(this);
        mMelodiesAdapter.setEmptyView(emptyView);
        mMelodiesAdapter.setOnEndlessListener(this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.melodies_recyclerview);
        recyclerView.setAdapter(mMelodiesAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getString(R.string.loading_artists));
    }
}
