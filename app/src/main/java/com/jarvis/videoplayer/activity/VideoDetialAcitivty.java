package com.jarvis.videoplayer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jarvis.jarvisvideoplayer.VideoPlayer;
import com.jarvis.jarvisvideoplayer.VideoPlayerController;
import com.jarvis.videoplayer.R;
import com.jarvis.videoplayer.adapter.CommitAdapter;
import com.jarvis.videoplayer.model.CommitBean;
import com.jarvis.videoplayer.model.VideoBean;
import com.jarvis.videoplayer.view.CommitRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Jarvis
 * @version 1.0
 * @title VideoPlayer
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/7/29 下午2:47
 * @changeRecord [修改记录] <br/>
 */

public class VideoDetialAcitivty extends AppCompatActivity {

    private VideoPlayer mVideoPlayer;
    private VideoPlayerController mController;
    private VideoBean mVideoBean;

    private CommitRecyclerView mRecyclerView;
    private CommitAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<CommitBean> mList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initData();
        initView();
    }

    private void initData() {
        mVideoBean = (VideoBean) getIntent().getSerializableExtra("playerbean");

        Random random = new Random();
        mList = new ArrayList<>();
        for (int i = 0; i < 20; i ++) {
            CommitBean bean = new CommitBean();
            StringBuilder name = new StringBuilder("用户");
            bean.count = i + random.nextInt(1000);
            bean.name = name.append(String.valueOf(random.nextInt(1000))).toString();
            bean.text = "我觉得写得很好了!" + i;
            mList.add(bean);
        }
    }

    private void initView() {
        mVideoPlayer = (VideoPlayer) findViewById(R.id.video_player);
        mController = new VideoPlayerController(this);
        mController.setTitle(mVideoBean.getTitle());
        mController.setImage(mVideoBean.getImageUrl());
        mVideoPlayer.setController(mController);
        mVideoPlayer.setData(mVideoBean.getVideoUrl(), null);

        mRecyclerView = (CommitRecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new CommitAdapter(this, mList);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        mRecyclerView.scheduleLayoutAnimation();
    }
}
