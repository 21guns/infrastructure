package com.guns21.feign.target;

import feign.Request;
import feign.RequestTemplate;
import feign.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.Objects;

import static feign.Util.checkNotNull;
import static feign.Util.emptyToNull;

/**
 * 使用feign调用http设置相关参数
 */
public class SpringSessionHeaderTokenTarget<T> implements Target<T> {
    private static Logger logger = LoggerFactory.getLogger(SpringSessionHeaderTokenTarget.class);
    private static final String HEADER_X_AUTH_TOKEN = "X-Auth-Token";

    private final String headerName;
    private final Class<T> type;
    private final String name;
    private final String url;

    public static <T> SpringSessionHeaderTokenTarget<T> newTarget(Class<T> type, String url) {
        return new SpringSessionHeaderTokenTarget(type, url);
    }

    private SpringSessionHeaderTokenTarget(Class<T> type, String url) {
        this(HEADER_X_AUTH_TOKEN,type, url, url);
    }

    private SpringSessionHeaderTokenTarget(String headerName, Class<T> type, String name, String url) {
        this.headerName = checkNotNull(headerName, "headerName");
        this.type = checkNotNull(type, "type");
        this.name = checkNotNull(emptyToNull(name), "name");
        this.url = checkNotNull(emptyToNull(url), "url");
    }

    @Override
    public Class type() {
        return type;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public Request apply(RequestTemplate input) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = requestAttributes.getRequest();
            if (Objects.nonNull(request)) {
                String headerValue = request.getHeader(this.headerName);
                if (null == headerValue) {
                    logger.warn("header's key [{}] is null ", headerName);
                } else {
                    input.header(headerName, headerValue);
                }
            } else {
                logger.warn("don't find request for requestAttributes.getRequest()");
            }
        } else {
            logger.warn("don't find ServletRequestAttributes for RequestContextHolder.getRequestAttributes()");
        }
        //@see HardCodedTarget
        if (input.url().indexOf("http") != 0) {
            input.insert(0, url());
        }
        return input.request();
    }
}
