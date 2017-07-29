package com.jarvis.videoplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jarvis.videoplayer.R;
import com.jarvis.videoplayer.model.CommitBean;

import java.util.List;

/**
 * @author Jarvis
 * @version 1.0
 * @title VideoPlayer
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/7/29 下午5:07
 * @changeRecord [修改记录] <br/>
 */

public class CommitAdapter extends RecyclerView.Adapter<CommitAdapter.InnerViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<CommitBean> mCommitList;

    public CommitAdapter(Context context, List<CommitBean> commitList) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mCommitList = commitList;
    }

    @Override
    public InnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_commit, parent, false);
        return new InnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InnerViewHolder holder, int position) {
        bindView(holder, position);
    }

    @Override
    public int getItemCount() {
        return mCommitList.size();
    }

    private void bindView(final InnerViewHolder holder, int position) {
        CommitBean commitBean = mCommitList.get(position);
        holder.mContent.setText(commitBean.text);
        holder.mName.setText(commitBean.name);
        holder.mCount.setText(String.valueOf(commitBean.count));
        Glide.with(mContext)
                .load("https://avatars3.githubusercontent.com/u/7964606?v=3&s=460")
                .asBitmap()
                .into(new BitmapImageViewTarget(holder.mAvatar) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.mAvatar.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    public static class InnerViewHolder extends RecyclerView.ViewHolder {

        private ImageView mAvatar;
        private TextView mContent;
        private TextView mCount;
        private TextView mName;

        public InnerViewHolder(View itemView) {
            super(itemView);
            mAvatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
            mName = (TextView) itemView.findViewById(R.id.ss_user);
            mCount = (TextView) itemView.findViewById(R.id.tvLikeCount);
            mContent = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
