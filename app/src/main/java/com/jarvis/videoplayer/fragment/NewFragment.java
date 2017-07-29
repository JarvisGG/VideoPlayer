package com.jarvis.videoplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarvis.videoplayer.R;
import com.jarvis.videoplayer.adapter.NewsAdapter;

/**
 * @author Jarvis
 * @version 1.0
 * @title VideoPlayer
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/7/29 下午8:53
 * @changeRecord [修改记录] <br/>
 */

public class NewFragment extends Fragment {

    public static final int PAGE_COUNT = 7;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private NewsAdapter mAdapter;

    public static NewFragment newInstance(String info) {
        return new NewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new, null);
        initView(view);
        return view;
    }

    private void initView(View containerView) {
        mTabLayout = (TabLayout) containerView.findViewById(R.id.main_pager_tabs);
        mViewPager = (ViewPager) containerView.findViewById(R.id.main_pager);

        mViewPager.setOffscreenPageLimit(PAGE_COUNT);
        mAdapter = new NewsAdapter(getActivity(), getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }
}
