package com.guns21.support.event;

import java.io.*;

/**
 * Created by jliu on 16/7/23.
 */
public class Event<T> implements Serializable {

    protected T source;

    public Event(T source) {
        this.source = source;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }
}
