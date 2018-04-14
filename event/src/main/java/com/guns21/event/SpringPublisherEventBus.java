package com.guns21.event;

import com.guns21.event.domain.AskEvent;
import com.guns21.event.domain.NotifyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @author jliu
 */
public class SpringPublisherEventBus implements EventBus {
    private static final Logger logger = LoggerFactory.getLogger(EventBus.class);

    private ApplicationEventPublisher applicationEventPublisher;

    public SpringPublisherEventBus(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(NotifyEvent event) {
        logger.debug("publish event for {}", event);
        applicationEventPublisher.publishEvent(event);

    }

    @Override
    public void ask(AskEvent event) {
        logger.debug("ask event for {}", event);
        throw new UnsupportedOperationException("unsupported ask");
    }
}
