package com.guns21.session.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * Created by ljj on 17/5/24.
 */
@Configuration
public class HttpSessionConfig {

  /**
   * 通过header传递session ID.
   */
  @Bean
  public HttpSessionStrategy httpSessionStrategy() {
    return new HeaderHttpSessionStrategy();
  }

  //    @Bean( name = "springSessionDefaultRedisSerializer")
  public RedisSerializer redisSerializer() {
    return new FastJsonSessionSerializer();
  }
}
