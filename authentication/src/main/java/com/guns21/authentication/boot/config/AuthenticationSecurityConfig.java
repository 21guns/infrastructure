package com.guns21.authentication.boot.config;

import com.guns21.authentication.provider.ext.AuthExtValidator;
import com.guns21.authentication.provider.service.HttpAuthenticationProvider;
import com.guns21.authentication.provider.service.MyAuthenticationFailureHandler;
import com.guns21.authentication.provider.service.MyAuthenticationSuccessHandler;
import com.guns21.authentication.provider.service.MyLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    private MyLogoutSuccessHandler myLogoutSuccessHandler;
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private AuthExtValidator authExtValidator;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(httpAuthenticationProvider());
    }

    @Bean
    public AuthenticationProvider httpAuthenticationProvider() {
        return new HttpAuthenticationProvider();
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
                .successHandler(myAuthenticationSuccessHandler).failureHandler(myAuthenticationFailureHandler)
                .and().logout().logoutUrl(lougout)
                .logoutSuccessHandler(myLogoutSuccessHandler).invalidateHttpSession(true)
                .and().csrf().disable();
    }
}
