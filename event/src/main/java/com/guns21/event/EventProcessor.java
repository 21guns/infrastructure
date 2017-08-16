package com.guns21.event;

import com.guns21.event.domain.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

/**
 * Created by jliu on 2017/6/5.
 */
@EnableBinding(EventBusClient.class)
public class EventProcessor implements ApplicationEventPublisherAware {
    private static final Logger logger = LoggerFactory.getLogger(EventProcessor.class);

    public static final String EVENT_TYPE = "eventType";

    @Autowired
    private BinderAwareChannelResolver binderAwareChannelResolver;

    @Autowired
    private EventBusClient busClient;

    private ApplicationEventPublisher applicationEventPublisher;


    /**
     * 动态创建 destination
     * @param message
     * @param destination
     * @return
     */
    public boolean sendMessage(BaseEvent message, String destination) {

        logger.debug("send message to kafka topic: {}, message: {}", destination, message);

        MessageChannel messageChannel = binderAwareChannelResolver.resolveDestination(destination);
        return messageChannel.send(MessageBuilder.withPayload(message)
                    .setHeader(EVENT_TYPE,message.getEventType())
                .build());

    }

    public boolean sendMessage(BaseEvent message) {

        return busClient.eventBusOutput().send(MessageBuilder.withPayload(message)
                    .setHeader(EVENT_TYPE,message.getEventType())
                .build());
    }

//    @StreamListener( EventBusClient.INPUT)
//    public void receiveMessage(Object payload) throws IllegalAccessException {
//        logger.debug("receive {}",payload);
//        this.applicationEventPublisher.publishEvent(payload);
//    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
