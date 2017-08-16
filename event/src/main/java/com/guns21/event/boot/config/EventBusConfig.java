package com.guns21.event.boot.config;

import com.guns21.event.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by jliu on 2017/6/6.
 */
@Configuration
//@EnableAsync
public class EventBusConfig {

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }

    //异步执行事件配置
//    @Bean
//    TaskExecutor taskExecutor() {
//        return new SimpleAsyncTaskExecutor();
//    }
}
