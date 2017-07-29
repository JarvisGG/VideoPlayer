package com.jarvis.videoplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarvis.videoplayer.R;
import com.jarvis.videoplayer.adapter.HomeContainerAdapter;
import com.jarvis.videoplayer.dialog.ChannelDialogFragment;
import com.jarvis.videoplayer.view.HomeContainerView;
import com.jarvis.videoplayer.view.TabView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jarvis
 * @version 1.0
 * @title VideoPlayer
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/7/27 下午3:08
 * @changeRecord [修改记录] <br/>
 */

public class HomeFragment extends Fragment implements TabView.EditTabBar {

    private TabView mTabView;
    private HomeContainerView mHomeContainerView;
    private HomeContainerAdapter mHomeContainerAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    public static HomeFragment newInstance(String info) {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initView(View containerView) {
        mTabView = (TabView) containerView.findViewById(R.id.home_tab_bar);
        mHomeContainerView = (HomeContainerView) containerView.findViewById(R.id.home_recyclerview);
        mTabView.registerEditCallBack(this);
    }

    private void initData() {
        String[] titleStr = getResources().getStringArray(R.array.home_title);
        List<TabView.Router> tabList = new ArrayList<>();
        for (int i = 0; i < titleStr.length; i++) {
            TabView.Router router = new TabView.Router();
            router.position = i;
            router.title = titleStr[i];
            tabList.add(router);
        }
        mTabView.notifyTabData(tabList);

        List<Integer> mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add(R.drawable.pic4);
            mList.add(R.drawable.pic5);
            mList.add(R.drawable.pic6);
        }
        mHomeContainerView.setCurrentItemPos(2);
        mHomeContainerAdapter = new HomeContainerAdapter(mList);
        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mHomeContainerView.setLayoutManager(mLinearLayoutManager);
        mHomeContainerView.setAdapter(mHomeContainerAdapter);
        mHomeContainerView.initOperator();
    }

    @Override
    public void editTabBarCallback() {
        ChannelDialogFragment dialogFragment = ChannelDialogFragment.newInstance();
        dialogFragment.show(getActivity().getFragmentManager(), "CHANNEL");
    }
}
