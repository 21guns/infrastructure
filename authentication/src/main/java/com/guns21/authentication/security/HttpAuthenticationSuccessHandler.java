package com.guns21.authentication.security;

import com.guns21.domain.result.light.Result;
import com.guns21.servlet.util.ResponseUtils;
import com.guns21.user.login.constant.LoginConstant;
import com.guns21.user.login.domain.UserInfo;
import com.guns21.user.login.domain.UserRoleDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * Created by ljj on 17/6/20.
 */
public class HttpAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private MessageSourceAccessor messageSourceAccessor;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
            throws IOException, ServletException {

        if (auth.getPrincipal() instanceof UserRoleDetails) {
            UserRoleDetails userRoleDetails = (UserRoleDetails) auth.getPrincipal();

            UserInfo loginUserInfo = new UserInfo(userRoleDetails.getUserId(), userRoleDetails.getUsername(),
                    userRoleDetails.getNickname(), userRoleDetails.getRoles(), userRoleDetails.getManagedUserIds());
            request.getSession(false).setAttribute(LoginConstant.LOGIN_USER, loginUserInfo);

            ResponseUtils.writeResponse(response,
                    Result.success(messageSourceAccessor.getMessage("com.guns21.security.message.login.success","登录成功")
                            , loginUserInfo));
        } else {
            throw new ServletException("don't support Principal " + auth.getPrincipal().getClass());
        }

    }


}
