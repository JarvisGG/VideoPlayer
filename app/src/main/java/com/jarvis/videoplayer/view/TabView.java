package com.jarvis.videoplayer.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarvis.jarvisvideoplayer.Utils;
import com.jarvis.videoplayer.R;
import com.jarvis.videoplayer.rx.RxBus;
import com.jarvis.videoplayer.rx.RxSchedulers;


import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * @author Jarvis
 * @version 1.0
 * @title VideoPlayer
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/7/27 上午10:52
 * @changeRecord [修改记录] <br/>
 */

public class TabView extends FrameLayout {

    private Context mContext;
    private int tabItemWidth = -1;

    private RecyclerView tabRecyclerView;
    private ImageView tabEditView;
    private Subscription mDisposable;

    private InnerLayoutManager mInnerLayoutManager;
    private InnerAdapter mInnerAdapter;
    private View currentView;

    public List<Router> mRouters;

    private EditTabBar mEditTabBar;

    public interface EditTabBar {
        void editTabBarCallback();
    }

    public TabView(Context context) {
        super(context);
        initView(context);
        initOperator(context);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initOperator(context);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initOperator(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TabView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
        initOperator(context);
    }

    private void initView(Context context) {
        mContext = context;
        tabRecyclerView = new RecyclerView(context);
        FrameLayout.LayoutParams revLayoutParams = new LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                Utils.dp2px(context, 50)
        );
        revLayoutParams.gravity = Gravity.TOP;
        revLayoutParams.rightMargin = Utils.dp2px(context, 32);
        this.addView(tabRecyclerView, revLayoutParams);

        tabEditView = new ImageView(context);
        tabEditView.setImageResource(R.drawable.addred_channel_titlbar);
        FrameLayout.LayoutParams reiLayoutParams = new LayoutParams(
                Utils.dp2px(context, 22),
                Utils.dp2px(context, 22)
        );
        reiLayoutParams.topMargin = Utils.dp2px(context, 12);
        reiLayoutParams.rightMargin = Utils.dp2px(context, 3);
        reiLayoutParams.gravity = Gravity.TOP|Gravity.RIGHT;
        this.addView(tabEditView, reiLayoutParams);
    }

    private void initOperator(Context context) {
        mInnerLayoutManager = new InnerLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        tabRecyclerView.setLayoutManager(mInnerLayoutManager);

        mRouters = new ArrayList<>();
        mInnerAdapter = new InnerAdapter(context, mRouters);
        tabRecyclerView.setAdapter(mInnerAdapter);

        tabEditView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditTabBar != null) {
                    mEditTabBar.editTabBarCallback();
                }
            }
        });
    }

    public void registerEditCallBack(EditTabBar editTabBar) {
        this.mEditTabBar = editTabBar;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mDisposable = RxBus.getInstance()
                .toObserverable(Router.class)
                .compose(RxSchedulers.threadSwitchSchedulers())
                .subscribe(new Subscriber<Router>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Router router) {
                        if (router.type == 1) {
                            tabRecyclerView.smoothScrollToPosition(
                                    router.position);
                            View targetChild = tabRecyclerView.getLayoutManager().findViewByPosition(router.position);
                            if (targetChild != null) {
                                changeCurrentStatus(targetChild);
                                TextView targetChildTv = (TextView) currentView.findViewById(R.id.tab_title);
                                targetChildTv.setTextColor(getResources().getColor(R.color.colorAccent));
                            }
                        }
                    }

                });
    }

    private void changeCurrentStatus(View targetChild) {
        if (currentView != null) {
            TextView chrrentChildTv = (TextView) currentView.findViewById(R.id.tab_title);
            if (chrrentChildTv != null) {
                chrrentChildTv.setTextColor(Color.BLACK);
            }
        }
        currentView = targetChild;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mDisposable.unsubscribe();
    }

    public void notifyTabData(List<Router> routers) {
        this.mRouters = routers;
        this.mInnerAdapter.setRouters(routers);
        this.mInnerAdapter.notifyItemRangeChanged(0, routers.size());
    }

    public static class InnerLayoutManager extends LinearLayoutManager {

        public InnerLayoutManager(Context context) {
            super(context);
        }

        public InnerLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public InnerLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }
    }

    public class InnerAdapter extends RecyclerView.Adapter<InnerViewHolder> {

        private Context mContext;
        private LayoutInflater mInflater;
        private List<Router> mRouters;

        public InnerAdapter(Context context, List<Router> routers) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(mContext);
            this.mRouters = routers;
        }

        @Override
        public InnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_tab, parent, false);
            tabItemWidth = view.getWidth();
            return new InnerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final InnerViewHolder holder, final int position) {
            holder.tabView.setText(mRouters.get(position).title);
            holder.tabView.setTextColor(Color.BLACK);
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    RxBus.getInstance().post(mRouters.get(position));
                    changeCurrentStatus(holder.tabView);
                    holder.tabView.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mRouters.size();
        }

        public void setRouters(List<Router> routers) {
            this.mRouters = routers;
        }

    }

    public class InnerViewHolder extends RecyclerView.ViewHolder {

        public TextView tabView;

        public InnerViewHolder(View itemView) {
            super(itemView);
            tabView = (TextView) itemView.findViewById(R.id.tab_title);
        }
    }

    public static class Router {
        public int type = 0;
        public int position;
        public String title;
    }
}
