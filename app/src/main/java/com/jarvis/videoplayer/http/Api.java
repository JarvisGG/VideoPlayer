//package com.jarvis.videoplayer.http;
//
//import org.json.JSONArray;
//
//import java.util.List;
//
//import io.reactivex.Flowable;
//import retrofit2.http.GET;
//import retrofit2.http.POST;
//import retrofit2.http.Path;
//
///**
// * @author Jarvis
// * @version 1.0
// * @title VideoPlayer
// * @description 该类主要功能描述
// * @company 北京奔流网络技术有限公司
// * @create 2017/7/29 下午10:23
// * @changeRecord [修改记录] <br/>
// */
//
//public interface Api {
//    @GET("before/{date}")
//    Flowable<String> getNews(@Path("date") String date);
//
//    @GET("{id}")
//    Flowable<String> getOfflineNews(@Path("id") int storyId);
//}
