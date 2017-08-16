package com.guns21.event;

import com.guns21.event.domain.NotifyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by jliu on 2017/6/2.
 */
public class EventBus {
    private static final Logger logger = LoggerFactory.getLogger(EventBus.class);

    @Value("${spring.application.name}")
    private String appName;
    @Autowired
    private EventProcessor eventProcessor;

    public void  publish(NotifyEvent event){
//        eventProcessor.sendMessage(event,event.getClass().getSimpleName());//每一个事件一个topic
//        eventProcessor.sendMessage(event,appName + EventConstants.EVENT_TOPIC__SUFFIX);//每一个服务一个topic
        eventProcessor.sendMessage(event);
    }
}
