package com.guns21.authentication.boot.config;

import com.guns21.authentication.provider.service.MyAuthenticationFailureHandler;
import com.guns21.authentication.provider.service.MyAuthenticationProvider;
import com.guns21.authentication.provider.service.MyAuthenticationSuccessHandler;
import com.guns21.authentication.provider.service.MyLogoutSuccessHandler;
import com.guns21.authentication.provider.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(httpAuthenticationProvider());
    }


    @Bean
    public MyUserDetailsService httpUserDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    public AuthenticationProvider httpAuthenticationProvider() {
        return new MyAuthenticationProvider().setMyUserDetailsService(httpUserDetailsService());
    }

    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .requestMatchers().antMatchers(login, lougout)
                .and().authorizeRequests().anyRequest().authenticated()
                .and().formLogin().loginProcessingUrl(login)
                .successHandler(myAuthenticationSuccessHandler).failureHandler(myAuthenticationFailureHandler)
                .and().logout().logoutUrl(lougout)
                .logoutSuccessHandler(myLogoutSuccessHandler).invalidateHttpSession(true)
                .and().csrf().disable();
    }
}
