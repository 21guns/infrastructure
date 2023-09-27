package com.guns21.authorization.security;

import com.guns21.domain.result.light.Result;
import com.guns21.servlet.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by ljj on 17/6/20.
 */
@Slf4j
public class HttpAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    private static final String HEADER_X_AUTH_TOKEN = "X-Auth-Token";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.warn("commence for url [{}] with exception {}", request.getRequestURI(), authException.toString());
        log.debug("commence ", authException);

        /**
         * 当启用匿名访问时，就会校验访问的资源的角色里是否包含匿名用户的角色，如不包含则抛出InsufficientAuthenticationException
         */
        if (authException instanceof InsufficientAuthenticationException) {
            ResponseUtils.writeResponse(response, Result.fail403("禁止访问"));
        }
        /**
         * 当禁用匿名访问时：1.没有传token 2.token过期 都会产生AuthenticationCredentialsNotFoundException异常
         */
        else if (authException instanceof AuthenticationCredentialsNotFoundException) {
            if (StringUtils.isNotEmpty(request.getHeader(HEADER_X_AUTH_TOKEN))) {
                ResponseUtils.writeResponse(response, Result.fail("902", messageSourceAccessor.getMessage("com.guns21.security.message.relogin", "请重新登录")));
            } else {
                ResponseUtils.writeResponse(response, Result.fail401(messageSourceAccessor.getMessage("com.guns21.security.message.login.commence", "请登录")));
            }

        } else {
            ResponseUtils.writeResponse(response, Result.fail401(messageSourceAccessor.getMessage("com.guns21.security.message.login.commence", "请登录")));
        }
    }

}
