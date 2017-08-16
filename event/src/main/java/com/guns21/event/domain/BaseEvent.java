package com.guns21.event.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by jliu on 2017/6/2.
 */
public abstract class BaseEvent<T> implements Serializable{

    protected String id;

    protected final long timestamp = System.currentTimeMillis();

    protected T source;

    protected String eventType;

    public BaseEvent(T source) {
        this.source = source;
        this.eventType = getClass().getSimpleName();
    }

    public BaseEvent() {
        this.eventType = getClass().getSimpleName();
    }
    /**
     * 获得Optional事件
     * @return
     */
    public Optional<T> getValue() {
        return Optional.ofNullable(source);
    }

    public T getSource() {
        return source;
    }

    public BaseEvent setSource(T source) {
        this.source = source;
        return this;
    }

    public String getId() {
        return id;
    }

    public BaseEvent setId(String id) {
        this.id = id;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public BaseEvent setEventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEvent)) return false;
        BaseEvent baseEvent = (BaseEvent) o;
        return Objects.equals(id, baseEvent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
