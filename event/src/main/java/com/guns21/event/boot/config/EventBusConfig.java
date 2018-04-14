package com.guns21.event.boot.config;

import com.guns21.event.EventBus;
import com.guns21.event.SpringPublisherEventBus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableAsync
public class EventBusConfig {

    @Bean
    @ConditionalOnMissingBean(EventBus.class)
    public EventBus eventBus(ApplicationEventPublisher applicationEventPublisher) {
        return new SpringPublisherEventBus(applicationEventPublisher);
    }

}
