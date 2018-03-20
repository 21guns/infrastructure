package com.guns21.user.login.boot.config;

import com.guns21.user.login.method.resolver.LoginUserMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class LoginUserMvcConfig extends WebMvcConfigurerAdapter {


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new LoginUserMethodArgumentResolver());
    }



}