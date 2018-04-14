package com.guns21.authentication.security;

import com.guns21.authentication.api.entity.AuthUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

public class PasswordEqualsAuthenticationProvider extends AbstractAuthenticationProvider {

    @Override
    protected void passwordValidate(AuthUser authUser, String password) throws AuthenticationException {

        if (StringUtils.isNoneEmpty(authUser.getPassword()) && !authUser.getPassword().equals(password)) {
            throw new BadCredentialsException(messageSourceAccessor.getMessage("com.guns21.security.message.password.error","用户密码错误"));
        }
    }
}

