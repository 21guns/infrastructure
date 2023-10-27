package com.guns21.authentication.security.provider;

import com.guns21.authentication.api.entity.AuthUser;
import com.guns21.authentication.api.service.UserAuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

/**
 * 验证码登录
 */
public class CodeAuthenticationProvider extends AbstractAuthenticationProvider {

    public CodeAuthenticationProvider(MessageSourceAccessor messageSourceAccessor, UserAuthService userAuthService) {
        super(messageSourceAccessor, userAuthService);
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

        return new CodeAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

