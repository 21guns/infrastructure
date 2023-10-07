package com.guns21.event.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by jliu on 2017/6/2.
 */
public abstract class BaseEvent<T> implements Serializable {

    /**
     * 该事件的唯一标示
     */
    protected String id;

    protected final long timestamp = System.currentTimeMillis();

    protected T source;

    protected String eventType;

    public BaseEvent(T source) {
        this();
        this.source = source;
    }

    public BaseEvent() {
        this.eventType = getClass().getSimpleName();
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseEvent)) {
            return false;
        }
        BaseEvent baseEvent = (BaseEvent) o;
        return Objects.equals(id, baseEvent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", source=" + source +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
