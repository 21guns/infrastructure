package com.guns21.authentication.provider.service;

import com.guns21.authentication.api.entity.MyUser;
import com.guns21.authentication.api.entity.Role;
import com.guns21.authentication.api.entity.UserRoleDetails;
import com.guns21.authentication.api.service.UserAuthService;
import com.guns21.common.helper.UserEncrypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ljj on 2017/6/17.
 */
public class HttpAuthenticationProvider implements AuthenticationProvider {
    @Value("${com.ktjr.security.message.password-error:用户密码错误！}")
    private String passwordError;

    @Value("${com.ktjr.security.message.login-error:请提交正确的用户信息！}")
    private String loginError;
    @Value("${com.ktjr.security.message.user-not-exist:用户不存在！}")
    private String userNotExistMessage;
    @Autowired
    private UserAuthService userAuthService;

    /**
     * 自定义用户认证
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //用户login提交的用户信息
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new BadCredentialsException(loginError);
        }

        //从库中获取用户信息
        MyUser myUser = userAuthService.getUser(username);

        if (myUser == null) {
            throw new UsernameNotFoundException(userNotExistMessage);
        }

        //比对用户密码,对密码进行加密之后进行密码比对。
        String encriptPassword = "";
        try {
            //TODO 密码校验
            encriptPassword = UserEncrypt.encryptUserPassword(myUser.getSalt(), password);
        } catch (Exception ex) {
        }

        if (encriptPassword.equals(myUser.getPassword())) {
            //生成认证对象
            List<Role> roles = userAuthService.getUserRoles(username);

            List<GrantedAuthority> grantedAuthorities = null;
            if (roles != null) {
                grantedAuthorities = roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
            }

            UserRoleDetails myUserDetails = new UserRoleDetails(myUser.getUserName(), myUser.getPassword(), grantedAuthorities);
            myUserDetails.setSalt(myUser.getSalt());
            myUserDetails.setUserId(myUser.getId());
            myUserDetails.setNickname(myUser.getNickname());
            myUserDetails.setOrganizationId(myUser.getOrganizationId());
            myUserDetails.setRoles(roles);

            Authentication auth = new UsernamePasswordAuthenticationToken(myUserDetails, myUserDetails.getPassword(), myUserDetails.getAuthorities());

            return auth;
        } else {
            throw new BadCredentialsException(passwordError);
        }
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }

}
