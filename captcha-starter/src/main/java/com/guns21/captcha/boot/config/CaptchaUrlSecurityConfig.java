package com.guns21.captcha.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 对产生验证码的url开放权限
 */
@Configuration
@EnableWebSecurity
@Order(20)
@ConfigurationProperties(prefix = "com.guns21.captcha")
public class CaptchaUrlSecurityConfig {

    //产生captcha的url
    private String url = "/api/v1/captcha";

    /**
     * 定义web的访问权限
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain captchaSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {


        httpSecurity.authorizeHttpRequests(authorize -> authorize.requestMatchers(url).permitAll());
        return httpSecurity.build();
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
