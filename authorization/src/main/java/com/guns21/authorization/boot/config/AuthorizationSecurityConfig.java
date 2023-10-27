package com.guns21.authorization.boot.config;

import com.guns21.authentication.boot.config.SecurityConfig;
import com.guns21.authorization.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import java.util.Objects;

/**
 * 鉴权
 */
@Configuration
@EnableWebSecurity
public class AuthorizationSecurityConfig {

    @Value("${com.guns21.security.permit.matcher:ant}")
    private String matcher;

    @Bean
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new HttpSessionInformationExpiredStrategy();
    }

    @Bean
    public AuthenticationEntryPoint httpAuthenticationEntryPoint() {
        return new HttpAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler httpAccessDeniedHandler() {
        return  new HttpAccessDeniedHandler();
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> httpAuthorizationManagerAdapter() {
        return new HttpAuthorizationManagerAdapter();
    }

    @Bean
    @Order(110)
    public SecurityFilterChain authorizationSecurityFilterChain(HttpSecurity httpSecurity, SecurityConfig securityConfig,
                                                                SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry) throws Exception {
        if (!securityConfig.isAnonymous()) {
            httpSecurity.anonymous(AbstractHttpConfigurer::disable);
        }
        httpSecurity
                .requestCache(cache -> cache.requestCache(new NullRequestCache()))//不缓存request
                .authorizeHttpRequests(authorize -> authorize.anyRequest().access(httpAuthorizationManagerAdapter()))
                .exceptionHandling(exception-> exception.authenticationEntryPoint(httpAuthenticationEntryPoint())
                                .accessDeniedHandler(httpAccessDeniedHandler())
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
    public WebSecurityCustomizer webSecurityCustomizer(SecurityConfig.SecurityPermitConfig securityPermitConfig) {
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
