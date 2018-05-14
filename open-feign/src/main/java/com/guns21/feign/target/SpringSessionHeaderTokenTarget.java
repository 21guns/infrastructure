package com.guns21.feign.target;

import feign.Request;
import feign.RequestTemplate;
import feign.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 使用feign调用http设置相关参数
 */
public class SpringSessionHeaderTokenTarget implements Target {
    private static Logger logger = LoggerFactory.getLogger(SpringSessionHeaderTokenTarget.class);
    private static final String HEADER_X_AUTH_TOKEN = "X-Auth-Token";

    private final String headerName;

    public SpringSessionHeaderTokenTarget() {
        this(HEADER_X_AUTH_TOKEN);
    }

    public SpringSessionHeaderTokenTarget(String headerName) {
        if (headerName == null) {
            throw new IllegalArgumentException("headerName cannot be null");
        }
        this.headerName = headerName;
    }

    @Override
    public Class type() {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String url() {
        return null;
    }

    @Override
    public Request apply(RequestTemplate input) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String headerValue = request.getHeader(this.headerName);
        if (null == headerValue) {
            logger.warn("header's key [{}] is null ", headerName);
        }
        input.header(headerName, headerValue);
        return input.request();
    }
}
