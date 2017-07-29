package com.jarvis.videoplayer.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jarvis.jarvisvideoplayer.VideoPlayer;
import com.jarvis.jarvisvideoplayer.VideoPlayerController;
import com.jarvis.videoplayer.R;
import com.jarvis.videoplayer.activity.MainActivity;
import com.jarvis.videoplayer.activity.VideoDetialAcitivty;
import com.jarvis.videoplayer.model.VideoBean;

import java.util.List;

/**
 * @author Jarvis
 * @version 1.0
 * @title VideoPlayer
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/6/1 下午2:32
 * @changeRecord [修改记录] <br/>
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private Context mContext;
    private List<VideoBean> mVideoList;

    public VideoAdapter(Context context, List<VideoBean> videoList) {
        mContext = context;
        mVideoList = videoList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
        VideoViewHolder holder = new VideoViewHolder(itemView);
        VideoPlayerController controller = new VideoPlayerController(mContext);
        holder.setController(controller);
        return holder;
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, final int position) {
        final VideoBean video = mVideoList.get(position);
        Glide.with(mContext)
                .load("https://avatars3.githubusercontent.com/u/7964606?v=3&s=460")
                .asBitmap()
                .into(new BitmapImageViewTarget(holder.mAvator) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.mAvator.setImageDrawable(circularBitmapDrawable);
                    }
                });
        holder.bindData(video);

        holder.mVideoDetial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoDetialAcitivty.class);
                intent.putExtra("playerbean", video);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.mVideoPlayer.setTransitionName("shareview");
                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation((Activity) mContext, holder.mVideoPlayer, "shareview");
                    mContext.startActivity(intent, options.toBundle());
                } else {
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        private VideoPlayerController mController;
        private VideoPlayer mVideoPlayer;
        private ImageView mAvator;
        private LinearLayout mVideoDetial;

        public VideoViewHolder(View itemView) {
            super(itemView);
            mVideoPlayer = (VideoPlayer) itemView.findViewById(R.id.video_player);
            mAvator = (ImageView) itemView.findViewById(R.id.ivAvatar);
            mVideoDetial = (LinearLayout) itemView.findViewById(R.id.video_data);
        }

        public void setController(VideoPlayerController controller) {
            mController = controller;
        }

        public void bindData(VideoBean videoBean) {
            mController.setTitle(videoBean.getTitle());
            mController.setImage(videoBean.getImageUrl());
            mVideoPlayer.setController(mController);
            mVideoPlayer.setData(videoBean.getVideoUrl(), null);
        }
    }
}
