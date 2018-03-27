package com.guns21.authorization.security;

import com.guns21.domain.result.light.Result;
import com.guns21.servlet.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by ljj on 17/6/20.
 */
@Slf4j
public class HttpAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
            log.warn("commence", authException);
        ResponseUtils.writeResponse(response, Result.fail401(messageSourceAccessor.getMessage("com.guns21.login.commence","请登录")));
    }

}
