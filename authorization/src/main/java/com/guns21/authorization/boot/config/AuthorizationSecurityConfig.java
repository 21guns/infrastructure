package com.guns21.authorization.boot.config;

import com.guns21.authorization.security.HttpAccessDecisionManager;
import com.guns21.authorization.security.HttpAccessDeniedHandler;
import com.guns21.authorization.security.HttpAuthenticationEntryPoint;
import com.guns21.authorization.security.HttpSessionInformationExpiredStrategy;
import com.guns21.authorization.security.RedisInvocationSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import java.util.Objects;

/**
 * 鉴权
 */
@Configuration
@EnableWebSecurity
@Order(101)
public class AuthorizationSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${com.guns21.security.permit.pages:#{null}}")
    private String[] permitPages;
    @Value("${com.guns21.security.permit.matcher:ant}")
    private String matcher;
    @Value("${com.guns21.security.anonymous.disable:true}")
    private boolean anonymous;
    @Value("${com.guns21.session.maximum:1}")
    private int maximumSessions;

    @Autowired
    private SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry;

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new HttpAccessDecisionManager();
    }

    @Bean
    public FilterInvocationSecurityMetadataSource securityMetadataSource() {
        return new RedisInvocationSecurityMetadataSource();
    }

    @Bean
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new HttpSessionInformationExpiredStrategy();
    }

    @Bean
    public HttpAuthenticationEntryPoint httpAuthenticationEntryPoint() {
        return new HttpAuthenticationEntryPoint();
    }

    protected void configure(HttpSecurity httpSecurity) throws Exception {
        if (anonymous) {
            httpSecurity.anonymous().disable();
        }

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
                .authenticationEntryPoint(httpAuthenticationEntryPoint())
                .accessDeniedHandler(new HttpAccessDeniedHandler())
                .and().csrf().disable();

        //同一个账户多次登录限制，对url访问进行监控
        httpSecurity
                .sessionManagement()
                .maximumSessions(maximumSessions)
//                .maxSessionsPreventsLogin(true) 为true是多次登录时抛出异常
                .sessionRegistry(springSessionBackedSessionRegistry)
                //被登录时，第一次返回的错误信息
                .expiredSessionStrategy(sessionInformationExpiredStrategy());


    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        WebSecurity.IgnoredRequestConfigurer ignoredRequestConfigurer = webSecurity
                .ignoring();
        //some help url
        ignoredRequestConfigurer.antMatchers("/error");
        if (Objects.nonNull(permitPages)) {
            if ("regex".equalsIgnoreCase(matcher)) {
                ignoredRequestConfigurer.regexMatchers(permitPages);
            } else if ("ant".equalsIgnoreCase(matcher)) {
                ignoredRequestConfigurer.antMatchers(permitPages);
            } else {
                ignoredRequestConfigurer.antMatchers(permitPages);
            }

        }
    }


}
