package com.guns21.web.filters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Liu Xiang on 2021/7/7.
 *
 * @author Liu Xiang
 */
@Data
public class AdvancedCommonsRequestLoggingFilter extends CommonsRequestLoggingFilter {

    protected String userSessionKey;

    protected List<String> userIdFieldNames;

    protected List<String> userNameFieldNames;

    protected boolean enabled = true;

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return enabled;
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }

    @Override
    protected String createMessage(HttpServletRequest request, String prefix, String suffix) {
        StringBuilder msg = new StringBuilder();
        msg.append(prefix);
        msg.append(request.getMethod()).append(" ");
        msg.append(request.getRequestURI());

        if (isIncludeQueryString()) {
            String queryString = request.getQueryString();
            if (queryString != null) {
                msg.append('?').append(queryString);
            }
        }

        if (isIncludeClientInfo()) {
            msg.append(getClientInfo(request));
        }

        if (isIncludeHeaders()) {
            HttpHeaders headers = new ServletServerHttpRequest(request).getHeaders();
            if (getHeaderPredicate() != null) {
                Enumeration<String> names = request.getHeaderNames();
                while (names.hasMoreElements()) {
                    String header = names.nextElement();
                    if (!getHeaderPredicate().test(header)) {
                        headers.set(header, "masked");
                    }
                }
            }
            msg.append(", headers=").append(headers);
        }

        if (isIncludePayload()) {
            String payload = getMessagePayload(request);
            if (payload != null) {
                msg.append(", payload=").append(payload);
            }
        }

        msg.append(suffix);
        return msg.toString();
    }

    private StringBuffer getClientInfo(HttpServletRequest request) {
        StringBuffer msg = new StringBuffer();
        String client = getClientIP(request);
        if (StringUtils.hasLength(client)) {
            msg.append(", client=").append(client);
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            msg.append(", session=").append(session.getId());
        }
        String user = getUser(request);
        if (user != null) {
            msg.append(", user=").append(user);
        }
        return msg;
    }

    private String getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return request.getRemoteUser();
        }

        Object user = request.getSession(false).getAttribute(userSessionKey);
        String userId = getUserId(user);
        String userName = getUserName(user);
        if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(userName)) {
            return request.getRemoteUser();
        }
        return String.format("id:%s,name:%s",
                Optional.ofNullable(userId).orElse(""),
                Optional.ofNullable(userName).orElse(""));
    }

    private String getUserId(Object user) {
        if (null == user) {
            return null;
        }

        Class<?> clazz = user.getClass();

        return userIdFieldNames.stream().map(fieldName ->
                Optional.ofNullable(ReflectionUtils.findField(clazz, fieldName)).map(field -> {
                    ReflectionUtils.makeAccessible(field);
                    return ReflectionUtils.getField(field, user);
                }).orElse(null)
        ).filter(Objects::nonNull).findFirst().map(Objects::toString).orElse(null);
    }

    private String getUserName(Object user) {
        if (null == user) {
            return null;
        }

        Class<?> clazz = user.getClass();

        return userNameFieldNames.stream().map(fieldName ->
            Optional.ofNullable(ReflectionUtils.findField(clazz, fieldName)).map(field -> {
                ReflectionUtils.makeAccessible(field);
                return ReflectionUtils.getField(field, user);
            }).orElse(null)
        ).filter(Objects::nonNull).findFirst().map(Objects::toString).orElse(null);
    }

    private String getClientIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");

        if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }

        return ipAddress;
    }

}
