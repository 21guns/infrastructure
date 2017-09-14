package com.guns21.authentication.security;

import com.guns21.authentication.api.entity.MyUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

public class SmsCodeAuthenticationProvider extends AbstractAuthenticationProvider {

    @Autowired
    private RedisTemplate<String, String> template;

    @Override
    protected void passwordValidate(MyUser myUser, String password) throws AuthenticationException {
        String smsCode = template.opsForValue().get(myUser.getUserName() + "-code");
        if (StringUtils.isNoneEmpty(smsCode) && !smsCode.equals(myUser.getPassword())) {
            throw new BadCredentialsException(passwordError);
        }
    }
}

