package com.guns21.user.login.boot.config;

import com.guns21.user.login.method.resolver.LoginUserMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 1.implements WebMvcRegistrations spring boot 2 support
 * 2.extend WebMvcConfigurerAdapter spring boot < 2
 */
@Configuration
public class LoginUserMvcConfig implements WebMvcConfigurer {


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoginUserMethodArgumentResolver());
    }



}