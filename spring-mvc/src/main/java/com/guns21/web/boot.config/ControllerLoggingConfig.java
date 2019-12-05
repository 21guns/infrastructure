package com.guns21.web.boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * controller打印日志
 * 1.引入 support 包
 * 2.yml增加配置
 * com:
 *   guns21:
 *     web:
 *       request:
 *         logging: true
 * 3.yml或logback.xml中增加日志级别
 * org.springframework.web.filter.CommonsRequestLoggingFilter = debug
 * @author jliu
 * @date 2019-11-20
 */
@Configuration
public class ControllerLoggingConfig {
    @Bean
    @ConditionalOnProperty(name="com.guns21.web.request.logging", havingValue="true")
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter crlf = new CommonsRequestLoggingFilter();
        crlf.setIncludeClientInfo(true);
        crlf.setIncludeQueryString(true);
        crlf.setIncludePayload(true);
        crlf.setMaxPayloadLength(2000);
        return crlf;
    }
}
