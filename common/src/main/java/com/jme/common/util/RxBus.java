package com.jme.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by gengda on 16/11/14.
 */

public class RxBus {

    private static volatile RxBus mInstance;

    private final SerializedSubject<Object, Object> bus; //普通消息容器
    private final SerializedSubject<Object, Object> busStick;  //sticky消息容器


    public RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
        busStick = new SerializedSubject<>(BehaviorSubject.create());
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


    /**
     * 发送普通消息
     *
     * @param object
     */
    public void post(Object object) {
        bus.onNext(object);
    }


    public void post(Object object, Object object2) {
        bus.onNext(new Message(object, object2));
    }


    /***
     * 发送sticky 消息
     * @param object
     */
    public void postSticky(Object object) {
        busStick.onNext(object);
    }

    public void postSticky(Object object, Object object2) {
        busStick.onNext(new Message(object,object2));
    }


    /**
     * 接收普通消息
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObserverable(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    /***
     * 接收sticky 消息
     * @param type
     * @param <T>
     * @return
     */
    public <T> Observable<T> toStickObservable(Class<T> type) {
        return busStick.asObservable().ofType(type).onBackpressureBuffer();
    }


    public class Message {
        private Object object;
        private Object object2;

        public Message() {
        }

        public Message(Object object, Object object2) {
            this.object = object;
            this.object2 = object2;
        }


        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        public Object getObject2() {
            return object2;
        }

        public void setObject2(Object object2) {
            this.object2 = object2;
        }
    }

}
