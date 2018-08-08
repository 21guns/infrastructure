package com.guns21.authorization.security;

import com.guns21.domain.result.light.Result;
import com.guns21.servlet.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class HttpSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        HttpServletResponse response = sessionInformationExpiredEvent.getResponse();
        ResponseUtils.writeResponse(response, Result.fail("901", messageSourceAccessor.getMessage("com.guns21.security.message.logined", "已经被最新登录替代")));
    }
}
