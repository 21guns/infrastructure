package com.guns21.authentication.boot.config;

import com.guns21.authentication.provider.ext.AuthExtValidator;
import com.guns21.authentication.provider.service.HttpAuthenticationProvider;
import com.guns21.authentication.provider.service.HttpAuthenticationFailureHandler;
import com.guns21.authentication.provider.service.HttpAuthenticationSuccessHandler;
import com.guns21.authentication.provider.service.HttpLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * 认证
 */
@Configuration
@EnableWebSecurity
@Order(100)
public class AuthenticationSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${com.ktjr.security.login:/login}")
    private String login;
    @Value("${com.ktjr.security.logout:/logout}")
    private String lougout;

    @Autowired
    private HttpLogoutSuccessHandler httpLogoutSuccessHandler;
    @Autowired
    private HttpAuthenticationSuccessHandler httpAuthenticationSuccessHandler;
    @Autowired
    private HttpAuthenticationFailureHandler httpAuthenticationFailureHandler;

    @Autowired
    private AuthExtValidator authExtValidator;

    @Bean
    public AuthenticationProvider httpAuthenticationProvider() {
        return new HttpAuthenticationProvider();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(httpAuthenticationProvider());
    }

    @Autowired
    RedisOperationsSessionRepository redisOperationsSessionRepository;
    @Bean
    SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry((FindByIndexNameSessionRepository) this.redisOperationsSessionRepository);
    }

    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .requestMatchers().antMatchers(login, lougout)
                .and().authorizeRequests().anyRequest().authenticated()
                .and()
                .addFilterBefore(new Filter() {
                    @Override
                    public void init(FilterConfig filterConfig) throws ServletException {
                    }

                    @Override
                    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
                        /**
                         * 通用扩展验证，用于其他项目在引用时，自定义validation
                         */
                        if (!authExtValidator.run((HttpServletRequest) request)) {
                            throw new InsufficientAuthenticationException(authExtValidator.getError());
                        }
                        filterChain.doFilter(request, response);
                    }

                    @Override
                    public void destroy() {
                    }
                }, UsernamePasswordAuthenticationFilter.class)
                .formLogin().loginProcessingUrl(login)
                .successHandler(httpAuthenticationSuccessHandler).failureHandler(httpAuthenticationFailureHandler)
                .and().logout().logoutUrl(lougout)
                .logoutSuccessHandler(httpLogoutSuccessHandler).invalidateHttpSession(true)
                .and().csrf().disable();

        //同一个账户多次登录限制，针对等是需要对之前的session进行表示
        httpSecurity
                .sessionManagement()
                .maximumSessions(1)
//                .maxSessionsPreventsLogin(true)为true是多次登录时抛出异常
                .sessionRegistry(sessionRegistry());

    }



//        @Bean
//    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
//        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
//    }
}
