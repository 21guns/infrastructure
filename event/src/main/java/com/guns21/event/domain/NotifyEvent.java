package com.guns21.event.domain;

/**
 * 通知事件
 * Created by jliu on 2017/6/5.
 */
public abstract class NotifyEvent<T> extends BaseEvent<T> {

    public NotifyEvent(T source) {
        super(source);
    }

    public NotifyEvent() {
        super();
    }
}