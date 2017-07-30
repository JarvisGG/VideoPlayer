package com.jarvis.videoplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jarvis.jarvisvideoplayer.Utils;
import com.jarvis.videoplayer.helper.HomeLinearSnapHelper;
import com.jarvis.videoplayer.rx.RxBus;
import com.jarvis.videoplayer.rx.RxSchedulers;

import rx.Subscriber;
import rx.Subscription;

/**
 * @author Jarvis
 * @version 1.0
 * @title VideoPlayer
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/7/27 上午11:33
 * @changeRecord [修改记录] <br/>
 */

public class HomeContainerView extends RecyclerView {

    private static final int FLING_MAX_VELOCITY = 8000; // 最大顺时滑动速度

    private HomeLinearSnapHelper mSnapHelper;

    private Context mContext;

    private float mScale = 0.9f; // 两边视图scale
    private int mPagePadding = 15; // 卡片的padding, 卡片间的距离等于2倍的mPagePadding
    private int mShowLeftCardWidth = 15;   // 左边卡片显示大小

    private int mCardWidth; // 卡片宽度
    private int mOnePageWidth; // 滑动一页的距离
    private int mCardGalleryWidth;

    private int mCurrentItemPos = 0;
    private int mCurrentItemOffset;

    private View currentTopView;
    private Subscription mDisposable;

    public HomeContainerView(Context context) {
        super(context);
        initView(context);
    }

    public HomeContainerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HomeContainerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
    }

    public void initOperator() {
        mSnapHelper = new HomeLinearSnapHelper();

        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mCurrentItemOffset += dx;
                computeCurrentItemPos();
                onScrolledChangedCallback();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    mSnapHelper.mNoNeedToScroll = mCurrentItemOffset == 0 || mCurrentItemOffset == getDestItemOffset(getAdapter().getItemCount() - 1);
                } else {
                    mSnapHelper.mNoNeedToScroll = false;
                }
            }
        });
        initWidth();
        mSnapHelper.attachToRecyclerView(this);
    }


    private int getDestItemOffset(int destPos) {
        return mOnePageWidth * destPos;
    }

    private void computeCurrentItemPos() {
        if (mOnePageWidth <= 0) return;
        boolean pageChanged = false;
        // 滑动超过一页说明已翻页
        if (Math.abs(mCurrentItemOffset - mCurrentItemPos * mOnePageWidth) >= mOnePageWidth) {
            pageChanged = true;
        }
        if (pageChanged) {
            mCurrentItemPos = mCurrentItemOffset / mOnePageWidth;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mDisposable = RxBus.getInstance()
                .toObserverable(TabView.Router.class)
                .compose(RxSchedulers.threadSwitchSchedulers())
                .subscribe(new Subscriber<TabView.Router>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TabView.Router router) {
                        if (router.type != 1) {
                            HomeContainerView.this.smoothScrollToPosition(
                                    router.position);
                        }
                    }
                });

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mDisposable.unsubscribe();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (currentTopView != null)
            super.drawChild(canvas, currentTopView, this.getDrawingTime());
    }
    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX = solveVelocity(velocityX);
        velocityY = solveVelocity(velocityY);
        return super.fling(velocityX, velocityY);
    }

    private int solveVelocity(int velocity) {
        if (velocity > 0) {
            return Math.min(velocity, FLING_MAX_VELOCITY);
        } else {
            return Math.max(velocity, -FLING_MAX_VELOCITY);
        }
    }

    private void initWidth() {
        this.post(new Runnable() {
            @Override
            public void run() {
                mCardGalleryWidth = HomeContainerView.this.getWidth();
                mCardWidth = mCardGalleryWidth - Utils.dp2px(mContext, 2 * (mPagePadding + mShowLeftCardWidth));
                mOnePageWidth = mCardWidth;
                HomeContainerView.this.smoothScrollToPosition(mCurrentItemPos);
                onScrolledChangedCallback();
            }
        });
    }

    private void onScrolledChangedCallback() {
        int offset = mCurrentItemOffset - mCurrentItemPos * mOnePageWidth;
        float percent = (float) Math.max(Math.abs(offset) * 1.0 / mOnePageWidth, 0.0001);

        Log.d("offset=%s, percent=%s", offset +"  "+ percent);
        View leftView = null;
        View currentView;
        View rightView = null;
        if (mCurrentItemPos > 0) {
            leftView = this.getLayoutManager().findViewByPosition(mCurrentItemPos - 1);
        }
        currentView = this.getLayoutManager().findViewByPosition(mCurrentItemPos);
        if (mCurrentItemPos < this.getAdapter().getItemCount() - 1) {
            rightView = this.getLayoutManager().findViewByPosition(mCurrentItemPos + 1);
        }

        if (leftView != null) {
            // y = (1 - mScale)x + mScale
            leftView.setScaleY((1 - mScale) * percent + mScale);
        }
        if (currentView != null) {
            // y = (mScale - 1)x + 1
            currentView.setScaleY((mScale - 1) * percent + 1);
        }
        if (rightView != null) {
            // y = (1 - mScale)x + mScale
            rightView.setScaleY((1 - mScale) * percent + mScale);
        }

        TabView.Router router = new TabView.Router();
        router.position = mCurrentItemPos;
        router.type = 1;
        RxBus.getInstance().post(router);

        currentTopView = currentView;
    }

    public void setCurrentItemPos(int currentItemPos) {
        this.mCurrentItemPos = currentItemPos;
    }

    public void setScale(float scale) {
        mScale = scale;
    }

    public void setPagePadding(int pagePadding) {
        mPagePadding = pagePadding;
    }

    public void setShowLeftCardWidth(int showLeftCardWidth) {
        mShowLeftCardWidth = showLeftCardWidth;
    }



}
