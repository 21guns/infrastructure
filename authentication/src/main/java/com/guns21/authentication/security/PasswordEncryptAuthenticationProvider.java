package com.guns21.authentication.security;

import com.guns21.authentication.api.entity.AuthUser;
import com.guns21.authentication.api.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码登录
 * Created by ljj on 2017/6/17.
 */
public class PasswordEncryptAuthenticationProvider extends AbstractAuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PasswordEncryptAuthenticationProvider(UserAuthService userAuthService) {
        super(userAuthService);
    }

    @Override
    protected void passwordValidate(AuthUser authUser, String password) throws AuthenticationException {
        if (!passwordEncoder.matches(authUser.getPasswordSalt() + password, authUser.getPassword())) {
            throw new BadCredentialsException(messageSourceAccessor.getMessage("com.guns21.security.message.password.error","用户密码错误"));
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
