package com.guns21.authentication.security;

import com.guns21.domain.result.light.Result;
import com.guns21.servlet.util.ResponseUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by ljj on 17/6/20.
 */
@Component
public class HttpAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        ResponseUtils.writeResponse(response, Result.fail401(exception.getMessage()));
    }
}
