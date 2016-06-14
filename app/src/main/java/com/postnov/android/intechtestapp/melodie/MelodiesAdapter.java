package com.postnov.android.intechtestapp.melodie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.postnov.android.intechtestapp.R;
import com.postnov.android.intechtestapp.data.entity.Melodie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by platon on 10.06.2016.
 */
public class MelodiesAdapter extends RecyclerView.Adapter<MelodiesAdapter.ViewHolder>
{
    private List<Melodie> mMelodieList;
    private View mEmptyView;
    private Context mContext;
    private int mItemId;
    private int mLastListSize;

    private OnItemClickListener mOnItemClickListener;
    private OnEndlessListener mOnEndlessListener;

    public MelodiesAdapter(Context context, int itemId)
    {
        mContext = context;
        mItemId = itemId;
        mMelodieList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(mItemId, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Melodie melodie = getList().get(position);

        holder.mArtist.setText(melodie.getArtist());
        holder.mMelodie.setText(melodie.getTitle());

        Glide.with(mContext).load(melodie.getPicUrl()).into(holder.mArtistPic);

        if (position == getItemCount() - 1)
        {
            mOnEndlessListener.loadMore(position);
        }
    }

    @Override
    public int getItemCount()
    {
        if (mMelodieList == null) return 0;
        return mMelodieList.size();
    }

    public List<Melodie> getList()
    {
        return mMelodieList;
    }

    public void swapList(List<Melodie> newList)
    {
        if (newList != null && mLastListSize != newList.size())
        {
            mMelodieList.addAll(newList);
            notifyDataSetChanged();
            mLastListSize = mMelodieList.size();
        }

        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public void setEmptyView(View view)
    {
        mEmptyView = view;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mArtistPic;
        public TextView mArtist;
        public TextView mMelodie;
        public LinearLayout mContainer;

        public ViewHolder(View view)
        {
            super(view);
            mArtistPic = (ImageView) view.findViewById(R.id.item_artist_pic);
            mArtist = (TextView) view.findViewById(R.id.item_artist_title);
            mMelodie = (TextView) view.findViewById(R.id.item_melodie_title);
            mContainer = (LinearLayout) view.findViewById(R.id.artist_container);
            mContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int adapterPosition = getAdapterPosition();
            mOnItemClickListener.onItemClick(v, adapterPosition);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mOnItemClickListener = listener;
    }

    public void setOnEndlessListener(OnEndlessListener listener)
    {
        mOnEndlessListener = listener;
    }

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    public interface OnEndlessListener
    {
        void loadMore(int position);
    }
}
