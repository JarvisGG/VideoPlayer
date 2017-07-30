package com.jarvis.videoplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarvis.model.Constants;
import com.jarvis.model.DailyNews;
import com.jarvis.model.NewsListFromZhihuObservable;
import com.jarvis.videoplayer.R;
import com.jarvis.videoplayer.adapter.NewsItemAdapter;
import com.jarvis.videoplayer.view.CommitRecyclerView;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author Jarvis
 * @version 1.0
 * @title VideoPlayer
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/7/29 下午9:40
 * @changeRecord [修改记录] <br/>
 */

public class NewsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private String mDate;
    private Boolean isToday;
    private boolean isRefreshed = false;
    private List<DailyNews> mNewsList = new ArrayList<>();

    private CommitRecyclerView mRecyclerView;
    private NewsItemAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private SwipeRefreshLayout mRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle bundle = getArguments();
            mDate = bundle.getString(Constants.BundleKeys.DATE);
            isToday = bundle.getBoolean(Constants.BundleKeys.IS_FIRST_PAGE);

            setRetainInstance(true);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, null);
        initView(view);
        return view;
    }

    private void initView(View containerView) {
        mRecyclerView = (CommitRecyclerView) containerView.findViewById(R.id.news_list);
        mRefreshLayout = (SwipeRefreshLayout) containerView.findViewById(R.id.swipe_refresh_layout);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NewsItemAdapter(getActivity(), mNewsList);
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.color_primary);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onRefresh() {

        NewsListFromZhihuObservable.ofDate(mDate)
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(dailyNewses -> {
                    this.mNewsList = dailyNewses;
                    isRefreshed = true;
                    mRefreshLayout.setRefreshing(false);
                    mAdapter.updateNewsList(dailyNewses);
                    mRecyclerView.scheduleLayoutAnimation();
                });


        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(true);
        }
    }



}
