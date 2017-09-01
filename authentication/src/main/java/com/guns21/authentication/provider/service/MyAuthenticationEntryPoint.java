package com.guns21.authentication.provider.service;

import com.guns21.result.domain.Result;
import com.guns21.servlet.util.ResponseUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.RedirectUrlBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by ljj on 17/6/20.
 */
public class MyAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public MyAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
//        String returnUrl = buildHttpReturnUrlForRequest(request);
//        request.getSession().setAttribute("backUrl", returnUrl);
        String error = authException.getMessage();
        if (error.contains("Full authentication")) {
            error = "用户未登录或会话过期！";
        }

//    Result<String> Result = Result.getCustomReturn(Result.NOT_AUTHENTICATION,
//        error,
//        "message",
//        null);
        ResponseUtils.writeResponse(response, Result.fail403(error));
    }

    protected String buildHttpReturnUrlForRequest(HttpServletRequest request)
            throws IOException, ServletException {

        RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();
        urlBuilder.setScheme("http");
        urlBuilder.setServerName(request.getServerName());
        urlBuilder.setPort(request.getServerPort());
        urlBuilder.setContextPath(request.getContextPath());
        urlBuilder.setServletPath(request.getServletPath());
        urlBuilder.setPathInfo(request.getPathInfo());
        urlBuilder.setQuery(request.getQueryString());

        return urlBuilder.getUrl();
    }
}
