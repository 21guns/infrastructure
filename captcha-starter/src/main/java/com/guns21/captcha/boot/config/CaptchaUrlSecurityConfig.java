package com.guns21.captcha.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 对产生验证码的url开放权限
 */
@Configuration
@EnableWebSecurity
@Order(2)
@ConfigurationProperties(prefix = "com.guns21.captcha")
public class CaptchaUrlSecurityConfig extends WebSecurityConfigurerAdapter {

    //产生captcha的url
    private String url = "/api/v1/captcha";

    /**
     * 定义web的访问权限
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {


        httpSecurity.requestMatchers().antMatchers(url).and().authorizeRequests().anyRequest().permitAll();
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
