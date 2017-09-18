package com.guns21.jedis.boot.config;

import com.guns21.jedis.FastJsonSerializer;
import com.guns21.jedis.JedisTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by jliu on 16/7/6.
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "spring.redis.pool")
public class JedisBootConfig extends JedisPoolConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;

    @Bean(name = "cacheRedis")
    public JedisTemplate cacheRedis() {
        if (StringUtils.hasText(password)) {
            return new JedisTemplate(this, host, port, password, new FastJsonSerializer());
        }
        return new JedisTemplate(this, host, port, new FastJsonSerializer());
    }

    @Bean(name = "persistedRedis")
    public JedisTemplate persistedRedis() {
        if (StringUtils.hasText(password)) {
            return new JedisTemplate(this, host, port, password, new FastJsonSerializer());
        }
        return new JedisTemplate(this, host, port, new FastJsonSerializer());
    }
}
