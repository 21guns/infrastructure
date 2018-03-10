package com.guns21.authentication.security;

import com.guns21.authentication.api.entity.UserRoleDetails;
import com.guns21.domain.result.light.Result;
import com.guns21.servlet.util.ResponseUtils;
import com.guns21.web.constant.SpringConstant;
import com.guns21.web.entity.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by ljj on 17/6/20.
 */
@Component
public class HttpAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${com.guns21.security.message.login-success:登录成功！}")
    private String loginMessage;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
            throws IOException, ServletException {

        if (auth.getPrincipal() instanceof UserRoleDetails) {
            UserRoleDetails userRoleDetails = (UserRoleDetails) auth.getPrincipal();

            UserInfo loginUserInfo = new UserInfo(userRoleDetails.getUserId(),
                    userRoleDetails.getUsername(), userRoleDetails.getNickname(), userRoleDetails.getRoles());
            request.getSession().setAttribute(SpringConstant.LOGIN_USER, loginUserInfo);

            ResponseUtils.writeResponse(response, Result.success(loginMessage, loginUserInfo));
        } else {
            throw new ServletException("don't support Principal " + auth.getPrincipal().getClass());
        }

    }


}
