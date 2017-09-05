package com.guns21.session.boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * Created by ljj on 17/5/24.
 */
@Configuration
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 5)
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 5 * 356 * 24 * 60 * 60)
public class HttpSessionConfig {

    /**
     * 通过header传递session ID.
     */
    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(RedisSerializer.class)
    public RedisSerializer springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
