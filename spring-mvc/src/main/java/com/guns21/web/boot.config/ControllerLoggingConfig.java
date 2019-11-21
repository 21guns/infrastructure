package com.guns21.web.boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * @author jliu
 * @date 2019-11-20
 */
@Configuration
public class ControllerLoggingConfig {
    @Bean
    @ConditionalOnProperty(name="logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter", havingValue="debug")
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter crlf = new CommonsRequestLoggingFilter();
        crlf.setIncludeClientInfo(true);
        crlf.setIncludeQueryString(true);
        crlf.setIncludePayload(true);
        crlf.setMaxPayloadLength(1000);
        return crlf;
    }
}
