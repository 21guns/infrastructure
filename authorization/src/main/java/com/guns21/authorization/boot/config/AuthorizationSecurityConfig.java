package com.guns21.authorization.boot.config;

import com.guns21.authentication.boot.config.SecurityConfig;
import com.guns21.authorization.security.HttpAccessDeniedHandler;
import com.guns21.authorization.security.HttpAuthenticationEntryPoint;
import com.guns21.authorization.security.HttpAuthorizationManagerAdapter;
import com.guns21.authorization.security.HttpSessionInformationExpiredStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
    @ConditionalOnExpression("${com.guns21.security.permit.authorize-any:false}")
    @Order(101)
    public SecurityFilterChain authorizationPermitSecurityFilterChain(HttpSecurity http, SecurityConfig securityConfig,
                                                                      SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry,
                                                                      SessionInformationExpiredStrategy sessionInformationExpiredStrategy) throws Exception {
            http
                    .authorizeHttpRequests(
                            authorize -> authorize
                                    .anyRequest().permitAll()
                    )
                    .csrf(csrf -> csrf.disable());


            //同一个账户多次登录限制，对url访问进行监控
            http
                    .sessionManagement(sessionManagement ->
                                    sessionManagement.maximumSessions(securityConfig.getMaximumSessions())
//                                .maxSessionsPreventsLogin(true) //为true是多次登录时抛出异常
                                            .sessionRegistry(springSessionBackedSessionRegistry)
                                            //被登录时，第一次返回的错误信息
                                            .expiredSessionStrategy(sessionInformationExpiredStrategy)
                    );
            return  http.build();
    }

    @Bean
    @Order(102)
    public SecurityFilterChain authorizationPermitUrlSecurityFilterChain(HttpSecurity httpSecurity, SecurityConfig.SecurityPermitConfig securityPermitConfig ) throws Exception {
        //permit urls
        if (Objects.nonNull(securityPermitConfig.getPermitPages())) {
            httpSecurity.securityMatchers((matchers) -> matchers.requestMatchers(securityPermitConfig.getPermitPages())
                            .requestMatchers("/error"))
                    .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        } else {
            httpSecurity.securityMatchers((matchers) -> matchers.requestMatchers("/error"))
                    .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        }
        return httpSecurity.build();
    }

    @Bean
    @Order(110)
    public SecurityFilterChain authorizationSecurityFilterChain(HttpSecurity httpSecurity, SecurityConfig securityConfig, SecurityConfig.SecurityPermitConfig securityPermitConfig,
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

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(SecurityConfig.SecurityPermitConfig securityPermitConfig) {
//        return  web -> {
//            WebSecurity.IgnoredRequestConfigurer ignoring = web.ignoring();
//            ignoring.requestMatchers("/error");
//            if (Objects.nonNull(securityPermitConfig.getPermitPages())) {
//                if ("regex".equalsIgnoreCase(matcher)) {
//                    ignoring.requestMatchers(securityPermitConfig.getPermitPages());
//                } else if ("ant".equalsIgnoreCase(matcher)) {
//                    ignoring.requestMatchers(securityPermitConfig.getPermitPages());
//                } else {
//                    ignoring.requestMatchers(securityPermitConfig.getPermitPages());
//                }
//            }
//        };
//    }


}
