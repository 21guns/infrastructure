package com.guns21.authentication.security;

import com.guns21.authentication.api.entity.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by ljj on 2017/6/17.
 */
public class PasswordEncryptAuthenticationProvider extends AbstractAuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    protected void passwordValidate(AuthUser authUser, String password) throws AuthenticationException {
        if (!passwordEncoder.matches(authUser.getPasswordSalt() + password, authUser.getPassword())) {
            throw new BadCredentialsException(messageSourceAccessor.getMessage("com.guns21.security.message.password.error","用户密码错误"));
        }
    }
}
