package com.jarvis.videoplayer.rx;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * @author Jarvis
 * @version 1.0
 * @title Fui_VideoChat
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/7/20 下午3:01
 * @changeRecord [修改记录] <br/>
 */

public class RxBus {
    private static volatile RxBus mInstance;
    private final Subject mBus;

    public RxBus() {
        mBus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getInstance() {
        if (mInstance == null) {
            synchronized (RxBus.class) {
                if (mInstance == null) {
                    mInstance = new RxBus();
                }
            }
        }
        return mInstance;
    }

    public void post(Object object) {
        mBus.onNext(object);
    }

    public <T> Observable<T> toObserverable(Class<T> eventType) {
        return mBus.ofType(eventType);
//        return mBus.filter(eventType::isInstance)
//                .cast(eventType);
    }
}
