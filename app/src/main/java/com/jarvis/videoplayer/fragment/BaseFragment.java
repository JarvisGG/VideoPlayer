package com.jarvis.videoplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jarvis.videoplayer.R;

/**
 * @author Jarvis
 * @version 1.0
 * @title VideoPlayer
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/7/27 上午1:05
 * @changeRecord [修改记录] <br/>
 */

public class BaseFragment extends Fragment {
    public static BaseFragment newInstance(String info) {
        Bundle args = new Bundle();
        BaseFragment fragment = new BaseFragment();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, null);
        TextView tvInfo = (TextView) view.findViewById(R.id.textView);
        tvInfo.setText(getArguments().getString("info"));
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Don't click me.please!.", Snackbar.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
