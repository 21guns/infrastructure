package com.guns21.authentication.provider.service;

import com.guns21.authentication.api.entity.MyLoginUserInfo;
import com.guns21.authentication.api.entity.UserRoleDetails;
import com.guns21.result.domain.Result;
import com.guns21.servlet.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by ljj on 17/6/20.
 */
@Component
public class HttpAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${com.ktjr.security.message.login-success:登录成功！}")
    private String loginMessage;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
            throws IOException, ServletException {
//        String backUrl = (String)request.getSession().getAttribute("backUrl");
//        request.getSession().removeAttribute("backUrl");

        MyLoginUserInfo myLoginUserInfo = null;
        if (auth.getPrincipal() instanceof UserRoleDetails) {
            UserRoleDetails myUserDetails = (UserRoleDetails) auth.getPrincipal();

            myLoginUserInfo = new MyLoginUserInfo();
            myLoginUserInfo.setId(myUserDetails.getUserId());
            myLoginUserInfo.setName(myUserDetails.getUsername());
            myLoginUserInfo.setNickname(myUserDetails.getNickname());
            myLoginUserInfo.setRoles(myUserDetails.getRoles());
        }

        ResponseUtils.writeResponse(response, Result.success(loginMessage, myLoginUserInfo));

        clearAuthenticationAttributes(request);
    }
}
