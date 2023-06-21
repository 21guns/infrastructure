package com.guns21.authorization.boot.config;

import com.guns21.authentication.boot.config.SecurityConfig;
import com.guns21.authorization.security.HttpAccessDecisionManager;
import com.guns21.authorization.security.HttpAccessDeniedHandler;
import com.guns21.authorization.security.HttpAuthenticationEntryPoint;
import com.guns21.authorization.security.HttpSessionInformationExpiredStrategy;
import com.guns21.authentication.filter.AccessFilter;
import com.guns21.authorization.security.RedisInvocationSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.Filter;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * 鉴权
 */
@Configuration
@EnableWebSecurity
@Order(110)
public class AuthorizationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${com.guns21.security.permit.matcher:ant}")
    private String matcher;

    @Autowired
    private SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry;

    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private SecurityConfig.SecurityPermitConfig securityPermitConfig;


    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new HttpAccessDecisionManager();
    }

    @Bean
    public FilterInvocationSecurityMetadataSource securityMetadataSource() {
        Map<RequestMappingInfo, HandlerMethod> pathMatcher = requestMappingHandlerMapping.getHandlerMethods();
        for(Iterator it = pathMatcher.keySet().iterator(); it.hasNext(); ) {
            RequestMappingInfo requestMappingInfo = (RequestMappingInfo) it.next();
            System.out.println(requestMappingInfo.toString());
//            new AntPathRequestMatcher(re)
        }

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
    @ConditionalOnMissingBean(name = "beforeAuthFilter")
    public Filter beforeAuthFilter() {
        return new AccessFilter();
    }

    protected void configure(HttpSecurity httpSecurity) throws Exception {
        if (!securityConfig.isAnonymous()) {
            httpSecurity.anonymous().disable();
        }

        httpSecurity
                .requestCache().requestCache(new NullRequestCache())//不缓存request
                .and()
                .addFilterBefore(beforeAuthFilter(), ChannelProcessingFilter.class)
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
                .maximumSessions(securityConfig.getMaximumSessions())
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
        if (Objects.nonNull(securityPermitConfig.getPermitPages())) {
            if ("regex".equalsIgnoreCase(matcher)) {
                ignoredRequestConfigurer.regexMatchers(securityPermitConfig.getPermitPages());
            } else if ("ant".equalsIgnoreCase(matcher)) {
                ignoredRequestConfigurer.antMatchers(securityPermitConfig.getPermitPages());
            } else {
                ignoredRequestConfigurer.antMatchers(securityPermitConfig.getPermitPages());
            }

        }
    }


}
