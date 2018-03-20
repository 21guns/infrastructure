package com.guns21.authentication.security;

import com.guns21.authentication.api.entity.AuthUser;
import com.guns21.authentication.api.service.UserAuthService;
import com.guns21.common.util.ObjectUtils;
import com.guns21.common.util.RegexChkUtils;
import com.guns21.user.login.domain.Role;
import com.guns21.user.login.domain.UserRoleDetails;
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
public abstract class AbstractAuthenticationProvider implements AuthenticationProvider {
    @Value("${com.guns21.security.message.password-error:用户密码错误！}")
    protected String passwordError;
    @Value("${com.guns21.security.message.login-error:请提交正确的用户信息！}")
    protected String loginError;
    @Value("${com.guns21.security.message.user-not-exist:用户不存在！}")
    protected String userNotExistMessage;
    @Value("${com.guns21.security.user-name-pattern:#{null}}")
    protected String userNamePattern;
    @Value("${com.guns21.security.password-pattern:#{null}}")
    protected String passwordPattern;
    @Autowired
    protected UserAuthService userAuthService;

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
        if (StringUtils.isNoneEmpty(userNamePattern)) {
            if (!RegexChkUtils.startCheck(userNamePattern, username)) {
                throw new BadCredentialsException("user name don't match pattern" + userNamePattern);
            }
        }
        if (StringUtils.isNoneEmpty(passwordPattern)) {
            if (!RegexChkUtils.startCheck(passwordPattern, password)) {
                throw new BadCredentialsException("password don't match pattern " + passwordPattern);
            }
        }
        //从库中获取用户信息
        AuthUser authUser = userAuthService.getUser(username);

        if (authUser == null) {
            throw new UsernameNotFoundException(userNotExistMessage);
        }

        passwordValidate(authUser, password);

        //生成认证对象
        List<Role> roles = userAuthService.getUserRoles(username);

        List<GrantedAuthority> grantedAuthorities = null;
        if (ObjectUtils.nonEmpty(roles)) {
            grantedAuthorities = roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
        }

        UserRoleDetails userRoleDetails = new UserRoleDetails(authUser.getUserName(), authUser.getPassword(), grantedAuthorities);
        userRoleDetails.setPasswordSalt(authUser.getPasswordSalt());
        userRoleDetails.setUserId(authUser.getId());
        userRoleDetails.setNickname(authUser.getNickName());
        userRoleDetails.setOrganizationId(authUser.getOrganizationId());
        userRoleDetails.setRoles(roles);

        Authentication auth = new UsernamePasswordAuthenticationToken(userRoleDetails, userRoleDetails.getPassword(), userRoleDetails.getAuthorities());

        return auth;
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }

    /**
     * 校验用户信息
     * @param authUser 登录用户信息
     * @param password 认证信息
     * @throws AuthenticationException 认证失败异常
     */
    protected abstract void passwordValidate(AuthUser authUser, String password) throws AuthenticationException;

}
