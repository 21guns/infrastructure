package com.guns21.authentication.security;

import com.guns21.authentication.api.entity.AuthUser;
import com.guns21.common.helper.UserEncrypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by ljj on 2017/6/17.
 */
public class PasswordEncryptAuthenticationProvider extends AbstractAuthenticationProvider {


    @Override
    protected void passwordValidate(AuthUser authUser, String password) throws AuthenticationException {
        //比对用户密码,对密码进行加密之后进行密码比对。
        String encriptPassword = "";
        try {
            //TODO 密码校验
            encriptPassword = UserEncrypt.encryptUserPassword(authUser.getSalt(), password);
        } catch (Exception ex) {
        }
        if (StringUtils.isNoneEmpty(encriptPassword) && !encriptPassword.equals(authUser.getPassword())) {
            throw new BadCredentialsException(passwordError);
        }
    }
}
