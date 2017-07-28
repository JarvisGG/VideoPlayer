package com.jarvis.videoplayer.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jarvis.videoplayer.R;
import com.jarvis.videoplayer.adapter.ChannelAdapter;
import com.jarvis.videoplayer.iterface.OnDragVHListener;
import com.jarvis.videoplayer.iterface.OnItemMoveListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.R.style.Theme_Black_NoTitleBar;
import static android.R.style.Theme_Light_NoTitleBar;

/**
 * @author Jarvis
 * @version 1.0
 * @title VideoPlayer
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/7/28 上午12:57
 * @changeRecord [修改记录] <br/>
 */

public class ChannelDialogFragment extends DialogFragment {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private ChannelAdapter mChannelAdapter;
    private ItemTouchHelper mHelper;
    private List<ChannelAdapter.ChannelEntity> items;
    private List<ChannelAdapter.ChannelEntity> otherItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, Theme_Light_NoTitleBar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            //添加动画
            dialog.getWindow().setWindowAnimations(R.style.dialogSlideAnim);
        }
        return inflater.inflate(R.layout.dialog_channel, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
    }

    private void initData() {
        String[] titleStr = getResources().getStringArray(R.array.home_title);
        String[] titleStrOther = getResources().getStringArray(R.array.home_total);
        items = new ArrayList<>();
        otherItems = new ArrayList<>();
        for (int i = 0; i < titleStr.length; i++) {
            ChannelAdapter.ChannelEntity channelEntity = new ChannelAdapter.ChannelEntity();
            channelEntity.setName(titleStr[i]);
            items.add(channelEntity);
        }
        for (int i = 0; i < titleStrOther.length; i++) {
            ChannelAdapter.ChannelEntity channelEntity = new ChannelAdapter.ChannelEntity();
            channelEntity.setName(titleStrOther[i]);
            otherItems.add(channelEntity);
        }
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags;
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if (manager instanceof GridLayoutManager || manager instanceof StaggeredGridLayoutManager) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                } else {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                }
                int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                if (viewHolder.getItemViewType() != target.getItemViewType()) {
                    return false;
                }

                if (recyclerView.getAdapter() instanceof OnItemMoveListener) {
                    OnItemMoveListener listener = ((OnItemMoveListener) recyclerView.getAdapter());
                    listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                }
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                // 不在闲置状态
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    if (viewHolder instanceof OnDragVHListener) {
                        OnDragVHListener itemViewHolder = (OnDragVHListener) viewHolder;
                        itemViewHolder.onItemSelected();
                    }
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof OnDragVHListener) {
                    OnDragVHListener itemViewHolder = (OnDragVHListener) viewHolder;
                    itemViewHolder.onItemFinish();
                }
                super.clearView(recyclerView, viewHolder);
            }

            @Override
            public boolean isLongPressDragEnabled() {
                // 不支持长按拖拽功能 手动控制
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                // 不支持滑动功能
                return false;
            }
        });
        mHelper.attachToRecyclerView(mRecyclerView);

        mChannelAdapter = new ChannelAdapter(getActivity(), mHelper, items, otherItems);
        mChannelAdapter.setOnMyChannelItemClickListener(new ChannelAdapter.OnMyChannelItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(getActivity(), items.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mChannelAdapter);

        mGridLayoutManager = new GridLayoutManager(getActivity(), 4);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = mChannelAdapter.getItemViewType(position);
                return viewType == ChannelAdapter.TYPE_MY || viewType == ChannelAdapter.TYPE_OTHER ? 1 : 4;
            }
        });
        mRecyclerView.setLayoutManager(mGridLayoutManager);
    }

    public static ChannelDialogFragment newInstance() {
        ChannelDialogFragment dialogFragment = new ChannelDialogFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("dataSelected", (Serializable) selectedDatas);
//        bundle.putSerializable("dataUnselected", (Serializable) unselectedDatas);
//        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

}
