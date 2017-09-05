package com.guns21.authentication.boot.config;

import com.guns21.authentication.provider.service.HttpAccessDeniedHandler;
import com.guns21.authentication.provider.service.HttpAuthenticationEntryPoint;
import com.guns21.authentication.provider.service.MyAccessDecisionManager;
import com.guns21.authentication.provider.service.MyInvocationSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.savedrequest.NullRequestCache;

/**
 * 鉴权
 */
@Configuration
@EnableWebSecurity
@Order(101)
public class AuthorizationSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${com.ktjr.security.permit-pages:null}")
    private String[] permitPages;

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new MyAccessDecisionManager();
    }

    @Bean
    public FilterInvocationSecurityMetadataSource securityMetadataSource() {
        return new MyInvocationSecurityMetadataSource();
    }

    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .requestCache().requestCache(new NullRequestCache())//不缓存request
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(securityMetadataSource());
                        return o;
                    }
                })
                .accessDecisionManager(accessDecisionManager())
                .and().exceptionHandling()
                .authenticationEntryPoint(new HttpAuthenticationEntryPoint())
                .accessDeniedHandler(new HttpAccessDeniedHandler())
                .and().csrf().disable();

    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity
                .ignoring()
                .antMatchers(permitPages);
    }


}
