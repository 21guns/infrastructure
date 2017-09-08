package com.guns21.authorization.boot.config;

import com.guns21.authorization.security.HttpAccessDecisionManager;
import com.guns21.authorization.security.HttpAccessDeniedHandler;
import com.guns21.authorization.security.HttpAuthenticationEntryPoint;
import com.guns21.authorization.security.RedisInvocationSecurityMetadataSource;
import com.guns21.authorization.security.HttpSessionInformationExpiredStrategy;
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
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

/**
 * 鉴权
 */
@Configuration
@EnableWebSecurity
@Order(101)
public class AuthorizationSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${com.guns21.security.permit-pages:null}")
    private String[] permitPages;


    @Autowired
    RedisOperationsSessionRepository redisOperationsSessionRepository;
    @Bean
    SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry((FindByIndexNameSessionRepository) this.redisOperationsSessionRepository);
    }

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


    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.anonymous().disable();
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

        //同一个账户多次登录限制，对url访问进行监控
        httpSecurity
                .sessionManagement()
                .maximumSessions(1)
//                .maxSessionsPreventsLogin(true) 为true是多次登录时抛出异常
                .sessionRegistry(sessionRegistry())
                .expiredSessionStrategy(sessionInformationExpiredStrategy());



    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity
                .ignoring()
                .antMatchers(permitPages);
    }


}
