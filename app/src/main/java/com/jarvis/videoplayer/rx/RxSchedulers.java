package com.jarvis.videoplayer.rx;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Jarvis
 * @version 1.0
 * @title Fui_VideoChat
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 17/2/10 下午4:07
 * @changeRecord [修改记录] <br/>
 */

public class RxSchedulers {
    public static <T> FlowableTransformer<T, T> threadSwitchSchedulers() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
