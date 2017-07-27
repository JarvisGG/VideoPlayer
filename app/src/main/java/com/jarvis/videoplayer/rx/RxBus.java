package com.jarvis.videoplayer.rx;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subscribers.SerializedSubscriber;

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
    private final FlowableProcessor<Object> mBus;

    public RxBus() {
        mBus = PublishProcessor.create().toSerialized();
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
        new SerializedSubscriber<>(mBus).onNext(object);
    }

    public <T> Flowable<T> toObserverable(Class<T> eventType) {
        return mBus.ofType(eventType);
//        return mBus.filter(eventType::isInstance)
//                .cast(eventType);
    }
}
