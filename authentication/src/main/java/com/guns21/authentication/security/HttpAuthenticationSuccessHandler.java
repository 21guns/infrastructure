package com.guns21.authentication.security;

import com.guns21.authentication.api.entity.Role;
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
import java.util.List;

/**
 * Created by ljj on 17/6/20.
 */
@Component
public class HttpAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${com.guns21.security.message.login-success:登录成功！}")
    private String loginMessage;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
            throws IOException, ServletException {

        if (auth.getPrincipal() instanceof UserRoleDetails) {
            UserRoleDetails userRoleDetails = (UserRoleDetails) auth.getPrincipal();

            LoginUserInfo loginUserInfo = new LoginUserInfo();
            loginUserInfo.setId(userRoleDetails.getUserId());
            loginUserInfo.setName(userRoleDetails.getUsername());
            loginUserInfo.setNickname(userRoleDetails.getNickname());
            loginUserInfo.setRoles(userRoleDetails.getRoles());

            ResponseUtils.writeResponse(response, Result.success(loginMessage, loginUserInfo));
        }

        clearAuthenticationAttributes(request);
    }


    /**
     * 由于用户登录成功后返回给客户端
     * Created by ljj on 17/7/14.
     */
    public class LoginUserInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        private String id;

        private String name;

        private String nickname;

        private List<Role> roles;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public List<Role> getRoles() {
            return roles;
        }

        public void setRoles(List<Role> roles) {
            this.roles = roles;
        }
    }
}
