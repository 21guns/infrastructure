package com.guns21.authentication.boot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.*;

/**
 * 认证
 */
@Configuration
@EnableWebSecurity
@Order(110)
public class ExtendSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${com.ktjr.security.login:/login}")
    private String login;
    @Value("${com.ktjr.security.logout:/logout}")
    private String lougout;




    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .addFilterAfter(new Filter() {
                    @Override
                    public void init(FilterConfig filterConfig) throws ServletException { }

                    @Override
                    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                    }

                    @Override
                    public void destroy() { }
                }, UsernamePasswordAuthenticationFilter.class);
    }
}
