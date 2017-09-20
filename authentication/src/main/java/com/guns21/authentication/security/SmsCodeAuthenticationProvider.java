package com.guns21.authentication.security;

import com.guns21.authentication.api.entity.AuthUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

public class SmsCodeAuthenticationProvider extends AbstractAuthenticationProvider {

    @Autowired
    private RedisTemplate<String, String> template;

    @Override
    protected void passwordValidate(AuthUser authUser, String password) throws AuthenticationException {
        String smsCode = template.opsForValue().get(authUser.getUserName() + ".code");
        if (StringUtils.isEmpty(smsCode)) {
            throw new BadCredentialsException("验证码错误");
        }
        if (StringUtils.isNoneEmpty(smsCode) && !smsCode.equals(authUser.getPassword())) {
            throw new BadCredentialsException(passwordError);
        }
    }
}

