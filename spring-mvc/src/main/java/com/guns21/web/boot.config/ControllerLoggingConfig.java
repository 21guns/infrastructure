package com.guns21.web.boot.config;

import com.guns21.web.filters.AdvancedCommonsRequestLoggingFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.Arrays;

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

    @Value("${com.guns21.web.request.user-session-key:loginUser}")
    private String userSessionKey;

    @Value("${com.guns21.web.request.user-id-field-names:id}")
    private String userIdFieldNames;

    @Value("${com.guns21.web.request.user-name-field-names:name,username,userName}")
    private String userNameFieldNames;

    @Bean
    @ConditionalOnProperty(name="com.guns21.web.request.logging", havingValue="true")
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        AdvancedCommonsRequestLoggingFilter crlf = new AdvancedCommonsRequestLoggingFilter();
        crlf.setEnabled(true);
        crlf.setIncludeClientInfo(true);
        crlf.setIncludeQueryString(true);
        crlf.setIncludePayload(true);
        crlf.setMaxPayloadLength(2000);

        if (StringUtils.isNotBlank(userSessionKey)) {
            crlf.setUserSessionKey(userSessionKey);
        }

        if (StringUtils.isNotBlank(userIdFieldNames)) {
            crlf.setUserIdFieldNames(Arrays.asList(userIdFieldNames.split(",")));
        }

        if (StringUtils.isNotBlank(userNameFieldNames)) {
            crlf.setUserNameFieldNames(Arrays.asList(userNameFieldNames.split(",")));
        }

        return crlf;
    }
}
