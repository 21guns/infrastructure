package com.guns21.authentication.security;

import com.guns21.result.domain.Result;
import com.guns21.servlet.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by ljj on 17/6/22.
 */
@Component
public class HttpLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    @Value("${com.ktjr.security.message.logout-success:登出成功！}")
    private String logoutMessage;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ResponseUtils.writeResponse(response, Result.success(logoutMessage));
    }
}
