package com.jarvis.videoplayer.rx;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    public static <T> Observable.Transformer<T, T> threadSwitchSchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
