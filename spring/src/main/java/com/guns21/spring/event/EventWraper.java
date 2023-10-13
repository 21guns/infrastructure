package com.guns21.spring.event;

import com.guns21.event.domain.BaseEvent;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static com.guns21.spring.event.EventConstant.EVENT_TYPE;

/**
 * @author jliu
 * @date 2019-11-29
 */
public final class EventWraper {

    public static  Message buildMessage(BaseEvent event) {
        return MessageBuilder.withPayload(event)
                .setHeader(EVENT_TYPE, event.getEventType())
                .build();
    }

    public static  MessageBuilder messageBuilder(BaseEvent event) {
        return MessageBuilder.withPayload(event)
                .setHeader(EVENT_TYPE, event.getEventType());
    }
}
