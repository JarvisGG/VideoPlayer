package com.jarvis.videoplayer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LayoutAnimationController;

/**
 * @author Jarvis
 * @version 1.0
 * @title VideoPlayer
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/7/29 下午6:01
 * @changeRecord [修改记录] <br/>
 */

public class CommitRecyclerView extends RecyclerView {
    public CommitRecyclerView(Context context) {
        super(context);
    }

    public CommitRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommitRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
        if (getAdapter() != null && getLayoutManager() instanceof LinearLayoutManager) {
            LayoutAnimationController.AnimationParameters parameters = params.layoutAnimationParameters;
            if (parameters == null){
                AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
                alphaAnimation.setDuration(1000);
                parameters = new LayoutAnimationController.AnimationParameters();
                params.layoutAnimationParameters = parameters;
            }
            parameters.count = count;
            parameters.index = index;
        } else {
            super.attachLayoutAnimationParameters(child, params, index, count);
        }
    }
}
