package com.jarvis.videoplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jarvis.videoplayer.R;
import com.jarvis.videoplayer.fragment.NewsListFragment;
import com.jarvis.videoplayer.http.Constants;
import com.jarvis.videoplayer.model.DailyNews;

import java.util.List;

/**
 * @author Jarvis
 * @version 1.0
 * @title VideoPlayer
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/7/29 下午11:14
 * @changeRecord [修改记录] <br/>
 */

public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.CardiewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<DailyNews> mNewsList;

    public NewsItemAdapter(Context context, List<DailyNews> newsList) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mNewsList = newsList;
    }

    @Override
    public CardiewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.news_list_item, parent, false);
        return new CardiewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardiewHolder holder, int position) {
        DailyNews dailyNews = mNewsList.get(position);
        Glide.with(mContext)
                .load(dailyNews.getThumbnailUrl())
                .into(holder.newsImage);
        if (dailyNews.getQuestions().size() > 1) {
            holder.questionTitle.setText(dailyNews.getDailyTitle());
            holder.dailyTitle.setText(Constants.Strings.MULTIPLE_DISCUSSION);
        } else {
            holder.questionTitle.setText(dailyNews.getQuestions().get(0).getTitle());
            holder.dailyTitle.setText(dailyNews.getDailyTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public void updateNewsList(List<DailyNews> newsList) {
        setNewsList(newsList);
        notifyDataSetChanged();
    }

    public void setNewsList(List<DailyNews> newsList) {
        this.mNewsList = newsList;
    }

    public static class CardiewHolder extends RecyclerView.ViewHolder {

        public ImageView newsImage;
        public TextView questionTitle;
        public TextView dailyTitle;
        public ImageView overflow;

        public CardiewHolder(View itemView) {
            super(itemView);
            newsImage = (ImageView) itemView.findViewById(R.id.thumbnail_image);
            questionTitle = (TextView) itemView.findViewById(R.id.question_title);
            dailyTitle = (TextView) itemView.findViewById(R.id.daily_title);
            overflow = (ImageView) itemView.findViewById(R.id.card_share_overflow);
        }
    }
}
