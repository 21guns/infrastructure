package com.guns21.data.boot.config;

import com.guns21.data.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工厂配置类
 * Created by jliu on 16/9/27.
 */
@Configuration
public class FactoryConfig {

    @Autowired
    private MessageSource messages;

    /**
     * @return
     */
    @Bean
    public ResultFactory resultFactory() {
        return new ResultFactory(messages);
    }
}
