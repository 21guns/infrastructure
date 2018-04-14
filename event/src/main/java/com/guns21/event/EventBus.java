package com.guns21.event;

import com.guns21.event.domain.AskEvent;
import com.guns21.event.domain.NotifyEvent;

public interface EventBus {

    /**
     * 广播事件
     * @param event
     */
    void publish(NotifyEvent event);


    /**
     * 点对点事件
     * @param event
     */
    void ask(AskEvent event);

}
