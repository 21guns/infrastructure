package com.guns21.authorization.boot.config;

import com.guns21.authentication.boot.config.SecurityConfig;
import com.guns21.authorization.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import java.util.Objects;

/**
 * 鉴权
 */
@Configuration
@EnableWebSecurity
@Order(110)
public class AuthorizationSecurityConfig {

    @Value("${com.guns21.security.permit.matcher:ant}")
    private String matcher;

    @Autowired
    private SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry;

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private SecurityConfig.SecurityPermitConfig securityPermitConfig;


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

    @Bean
    public AccessDecisionManagerAuthorizationManagerAdapter accessDecisionManagerAuthorizationManagerAdapter() {
        return new AccessDecisionManagerAuthorizationManagerAdapter(accessDecisionManager(), securityMetadataSource());
    }

    @Bean
    public SecurityFilterChain authorizationSecurityFilterChain(HttpSecurity httpSecurity,ApplicationContext applicationContext) throws Exception {
        if (!securityConfig.isAnonymous()) {
            httpSecurity.anonymous(AbstractHttpConfigurer::disable);
        }
        httpSecurity
                .requestCache(cache -> cache.requestCache(new NullRequestCache()))//不缓存request
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .exceptionHandling(exception-> exception.authenticationEntryPoint(httpAuthenticationEntryPoint())
//                                .accessDeniedHandler(new HttpAccessDeniedHandler())
                )
                .csrf(csrf -> csrf.disable());

        //同一个账户多次登录限制，对url访问进行监控
        httpSecurity
                .sessionManagement(sessionManagement ->
                        sessionManagement.maximumSessions(securityConfig.getMaximumSessions())
//                                .maxSessionsPreventsLogin(true) //为true是多次登录时抛出异常
                                .sessionRegistry(springSessionBackedSessionRegistry)
                                //被登录时，第一次返回的错误信息
                                .expiredSessionStrategy(sessionInformationExpiredStrategy()));

        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return  web -> {
            WebSecurity.IgnoredRequestConfigurer ignoring = web.ignoring();
            ignoring.requestMatchers("/error");
            if (Objects.nonNull(securityPermitConfig.getPermitPages())) {
                if ("regex".equalsIgnoreCase(matcher)) {
                    ignoring.requestMatchers(securityPermitConfig.getPermitPages());
                } else if ("ant".equalsIgnoreCase(matcher)) {
                    ignoring.requestMatchers(securityPermitConfig.getPermitPages());
                } else {
                    ignoring.requestMatchers(securityPermitConfig.getPermitPages());
                }
            }
        };
    }


}
