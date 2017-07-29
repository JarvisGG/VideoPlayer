package com.jarvis.videoplayer.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.jarvis.videoplayer.R;
import com.jarvis.videoplayer.fragment.NewFragment;
import com.jarvis.videoplayer.fragment.NewsListFragment;
import com.jarvis.videoplayer.http.Constants;

import java.text.DateFormat;
import java.util.Calendar;

import static com.jarvis.videoplayer.fragment.NewFragment.PAGE_COUNT;

/**
 * @author Jarvis
 * @version 1.0
 * @title VideoPlayer
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/7/29 下午9:36
 * @changeRecord [修改记录] <br/>
 */

public class NewsAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public NewsAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        Fragment newFragment = new NewsListFragment();

        Calendar dateToGetUrl = Calendar.getInstance();
        dateToGetUrl.add(Calendar.DAY_OF_YEAR, 1 - position);
        String date = Constants.Dates.simpleDateFormat.format(dateToGetUrl.getTime());

        Log.e("VideoPlayer-------->", date+"");
        bundle.putString(Constants.BundleKeys.DATE, date);
        bundle.putBoolean(Constants.BundleKeys.IS_FIRST_PAGE, position == 0);
        bundle.putBoolean(Constants.BundleKeys.IS_SINGLE, false);

        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Calendar displayDate = Calendar.getInstance();
        displayDate.add(Calendar.DAY_OF_YEAR, -position);

        return (position == 0 ? mContext.getString(R.string.zhihu_daily_today) + " " : "")
                + DateFormat.getDateInstance().format(displayDate.getTime());
    }
}
