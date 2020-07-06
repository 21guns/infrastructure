package com.guns21.session.boot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import javax.annotation.PostConstruct;

/**
 * Created by ljj on 17/5/24.
 */
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 5)
@Configuration
public class HttpSessionConfig extends RedisHttpSessionConfiguration {

    // unit minutes
    @Value("${com.guns21.session.timeout:30}")
    private int sessionTimeout;

    /**
     * 通过header传递session ID.
     */
//    @Bean
//    public HttpSessionStrategy httpSessionStrategy() {
//        return new HeaderHttpSessionStrategy();
//    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }

    @Bean
    @ConditionalOnMissingBean(RedisSerializer.class)
    public RedisSerializer springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @PostConstruct
    @Override
    public void init() {
        super.init();
        super.setMaxInactiveIntervalInSeconds(sessionTimeout * 60);
    }

}
