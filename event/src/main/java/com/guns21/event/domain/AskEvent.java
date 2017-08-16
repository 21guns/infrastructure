package com.guns21.event.domain;

/**
 * 点对点事件
 * Created by jliu on 2017/6/5.
 */
public abstract class AskEvent<T> extends BaseEvent<T>{
    public AskEvent(T source) {
        super(source);
    }

    public AskEvent() {
    }
}