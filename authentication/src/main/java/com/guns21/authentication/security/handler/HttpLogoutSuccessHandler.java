package com.guns21.authentication.security.handler;

import com.guns21.domain.result.light.Result;
import com.guns21.servlet.util.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by ljj on 17/6/22.
 */
public class HttpLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    private MessageSourceAccessor messageSourceAccessor;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        ResponseUtils.writeResponse(response, Result.success(messageSourceAccessor.getMessage("com.guns21.security.message.logout.success","登出成功")));
    }
}