package com.ktjr.security.provider.service;

import com.guns21.result.domain.Result;
import com.ktjr.security.api.entity.MyLoginUserInfo;
import com.ktjr.security.api.entity.MyUserDetails;
import com.ktjr.security.provider.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by ljj on 17/6/20.
 */
@Service
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${com.ktjr.security.message.login-success:登录成功！}")
    private String loginMessage;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
            throws IOException, ServletException {
//        String backUrl = (String)request.getSession().getAttribute("backUrl");
//        request.getSession().removeAttribute("backUrl");

        MyLoginUserInfo myLoginUserInfo = null;
        if (auth.getPrincipal() instanceof MyUserDetails) {
            MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();

            myLoginUserInfo = new MyLoginUserInfo();
            myLoginUserInfo.setId(myUserDetails.getUserId());
            myLoginUserInfo.setName(myUserDetails.getUsername());
            myLoginUserInfo.setNickname(myUserDetails.getNickname());
            myLoginUserInfo.setRoles(myUserDetails.getRoles());
        }

        ResponseUtil.writeResponse(response, Result.success(loginMessage, myLoginUserInfo));

        clearAuthenticationAttributes(request);
    }
}
