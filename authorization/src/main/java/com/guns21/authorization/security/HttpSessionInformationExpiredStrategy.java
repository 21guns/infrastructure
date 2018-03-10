package com.guns21.authorization.security;

import com.guns21.domain.result.light.Result;
import com.guns21.servlet.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class HttpSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Value("${com.guns21.security.logined:已经被最新登录请求替代}")
    private String accessDeniedMessage;

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        HttpServletResponse response = sessionInformationExpiredEvent.getResponse();
        ResponseUtils.writeResponse(response, Result.fail403(accessDeniedMessage));
    }
}
