package com.guns21.authentication.security;

import com.guns21.authentication.api.entity.AuthUser;
import com.guns21.authentication.api.service.UserAuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

/**
 * 验证码登录
 */
public class PasswordEqualsAuthenticationProvider extends AbstractAuthenticationProvider {

    public PasswordEqualsAuthenticationProvider(UserAuthService userAuthService) {
        super(userAuthService);
    }

    @Override
    protected void passwordValidate(AuthUser authUser, String password) throws AuthenticationException {

        if (StringUtils.isEmpty(password)){
            throw new BadCredentialsException(messageSourceAccessor.getMessage("com.guns21.security.message.smscode.empty","请输入验证码"));
        }
        if (Objects.nonNull(authUser.getPassword()) && !authUser.getPassword().equals(password)) {
            throw new BadCredentialsException(messageSourceAccessor.getMessage("com.guns21.security.message.smscode.error","验证码错误"));
        }
    }

    @Override
    protected Authentication buildAuthentication(UserDetails userDetails) {

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

