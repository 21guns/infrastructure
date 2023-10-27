package com.guns21.authentication.security.handler;

import com.guns21.domain.result.light.Result;
import com.guns21.servlet.util.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
