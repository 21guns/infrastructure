package com.guns21.authentication.boot.config;

import com.guns21.authentication.provider.service.MyAccessDeniedHandler;
import com.guns21.authentication.provider.service.HttpAuthenticationEntryPoint;
import com.guns21.authentication.provider.service.MyFilterSecurityInterceptor;
import com.guns21.authentication.provider.service.MyLogoutSuccessHandler;
import com.guns21.authentication.provider.service.MyUsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by ljj on 2017/6/17.
 * Create Spring Security Java Configuration.
 * The configuration creates a Servlet Filter known as the springSecurityFilterChain
 * which is responsible for all the security (protecting the application URLs,
 * validating submitted username and passwords, redirecting to the log in form, etc)
 * within your application.
 */
//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${com.ktjr.security.permit-pages:null}")
    private String[] permitPages;

    @Value("${com.ktjr.security.message.access-denied:没有访问权限！}")
    private String accessDeniedMessage;

    @Value("${com.ktjr.security.login-form-url:/loginpage}")
    private String loginFormUrl;

    @Value("${com.ktjr.security.login-enabled:false}")
    private boolean loginEnabled;

    @Value("${com.ktjr.security.logout:/logout}")
    private String lougout;


    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

    @Autowired
    private MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter;


    /**
     * 定义web的访问权限
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        //指定授权范围
        httpSecurity
//                .addFilterAt(myFilterSecurityInterceptor, FilterSecurityInterceptor.class)
                .addFilterAt(myUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler)
                .authenticationEntryPoint(new HttpAuthenticationEntryPoint())
                .and()
                .logout()
                .logoutUrl(lougout)
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .invalidateHttpSession(true)
                .permitAll();

        if (permitPages != null) {
            httpSecurity
                    .authorizeRequests()
                    .antMatchers(permitPages)
                    .permitAll();
        }
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        if(permitPages != null){
//            web.ignoring().antMatchers(permitPages);
//        }
//    }

}
