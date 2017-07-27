package com.jarvis.jarvisvideoplayer;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import static com.jarvis.jarvisvideoplayer.VideoPlayer.PLAYER_TINY_WINDOW;

/**
 * @author Jarvis
 * @version 1.0
 * @title VideoPlayer
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/6/12 下午7:29
 * @changeRecord [修改记录] <br/>
 */

public class VideoPlayerContainerLayout extends FrameLayout {

    private ViewDragHelpImpl mDragHelper;
    private VideoPlayer mVideoPlayer;

    public VideoPlayerContainerLayout(Context context) {
        super(context);
        initOperator();
    }

    public VideoPlayerContainerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initOperator();
    }

    public VideoPlayerContainerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initOperator();
    }

    public void setVideoPlayer(VideoPlayer videoPlayer) {
        this.mVideoPlayer = videoPlayer;
    }

    private void initOperator() {

        mDragHelper = new ViewDragHelpImpl.Builder()
                .create(this, 1.0f, new ViewDragHelper.Callback() {
                    @Override
                    public boolean tryCaptureView(View child, int pointerId) {
                        if (VideoPlayerContainerLayout.this == child) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public int clampViewPositionHorizontal(View child, int left, int dx) {
                        return left;
                    }

                    @Override
                    public int clampViewPositionVertical(View child, int top, int dy) {
                        return top;
                    }
                })
                .builder();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mVideoPlayer.getmPlayerState() == PLAYER_TINY_WINDOW) {
            final int action = MotionEventCompat.getActionMasked(ev);
//            if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
//                mDragHelper.viewDragHelper.cancel();
//                return false;
//            }
            return mDragHelper.viewDragHelper.shouldInterceptTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVideoPlayer.getmPlayerState() == PLAYER_TINY_WINDOW) {
            mDragHelper.viewDragHelper.processTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }

}
