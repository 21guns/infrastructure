package com.guns21.user.login.annotation;

import com.guns21.user.login.constant.LoginConstant;

import java.lang.annotation.*;

@Target( {ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

    /**
     * 当前用户在request中的名字.
     */
    String value() default LoginConstant.LOGIN_USER;

    /**
     * 当为true是，当前用户为空，抛出异常
     * @return
     */
    boolean required() default true;

}
