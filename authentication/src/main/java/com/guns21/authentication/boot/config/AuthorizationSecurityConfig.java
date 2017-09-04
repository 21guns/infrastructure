package com.guns21.authentication.boot.config;

import com.guns21.authentication.provider.service.HttpAuthenticationEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 鉴权
 */
@Configuration
@EnableWebSecurity
@Order(101)
public class AuthorizationSecurityConfig extends WebSecurityConfigurerAdapter {


    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity

                .authorizeRequests().anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(new HttpAuthenticationEntryPoint())
                .and().csrf().disable();
    }

}
