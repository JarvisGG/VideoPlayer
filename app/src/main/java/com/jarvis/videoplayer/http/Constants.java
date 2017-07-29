package com.jarvis.videoplayer.http;

import com.google.gson.reflect.TypeToken;
import com.jarvis.videoplayer.model.DailyNews;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class Constants {
    private Constants() {

    }

    public static final class Urls {
        public static final String BASE_URL = "http://news.at.zhihu.com/api/4/news/";
        public static final String ZHIHU_DAILY_BEFORE = "http://news.at.zhihu.com/api/4/news/before/";
        public static final String ZHIHU_DAILY_OFFLINE_NEWS = "http://news-at.zhihu.com/api/4/news/";
        public static final String ZHIHU_DAILY_PURIFY_BEFORE = "http://zhihudailypurify.herokuapp.com/news/";
        public static final String SEARCH = "http://zhihudailypurify.herokuapp.com/search/";
    }

    public static final class Dates {
        public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        @SuppressWarnings("deprecation")
        public static final Date birthday = new java.util.Date(113, 4, 19); // May 19th, 2013
    }

    public static final class Types {
        public static final Type newsListType = new TypeToken<List<DailyNews>>() {

        }.getType();
    }

    public static final class Strings {
        public static final String ZHIHU_QUESTION_LINK_PREFIX = "http://www.zhihu.com/question/";
        public static final String SHARE_FROM_ZHIHU = " 分享自知乎网";
        public static final String MULTIPLE_DISCUSSION = "这里包含多个知乎讨论，请点击后选择";
    }

    public static final class Information {
        public static final String ZHIHU_PACKAGE_ID = "com.zhihu.android";
    }

    public static final class SharedPreferencesKeys {
        public static final String KEY_SHOULD_ENABLE_ACCELERATE_SERVER = "enable_accelerate_server?";
        public static final String KEY_SHOULD_USE_CLIENT = "using_client?";
        public static final String KEY_SHOULD_AUTO_REFRESH = "auto_refresh?";
        public static final String KEY_SHOULD_USE_ACCELERATE_SERVER = "using_accelerate_server?";
    }

    public static final class BundleKeys {
        public static final String DATE = "date";
        public static final String IS_SINGLE = "single?";
        public static final String IS_FIRST_PAGE = "first_page?";
    }
}
