package com.guns21.authentication.boot.config;

import com.guns21.authentication.api.service.UserAuthService;
import com.guns21.authentication.ext.AuthExtValidator;
import com.guns21.authentication.filter.AccessFilter;
import com.guns21.authentication.filter.ValidatorFilter;
import com.guns21.authentication.security.HttpAuthenticationFailureHandler;
import com.guns21.authentication.security.HttpAuthenticationSuccessHandler;
import com.guns21.authentication.security.HttpLogoutSuccessHandler;
import com.guns21.authentication.security.PasswordEncryptAuthenticationProvider;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import java.io.IOException;

/**
 * 认证
 * migration to  6.0
 * see https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 * see https://www.baeldung.com/spring-deprecated-websecurityconfigureradapter
 */
@Configuration
@EnableWebSecurity
@Order(100)
//@ConfigurationProperties(prefix = "com.guns21.security") TODO 当有多处需要注入相同的prefix时不能使用ConfigurationProperties注入
public class AuthenticationSecurityConfig {
    @Value("${com.guns21.security.login:/login}")
    private String login;
    @Value("${com.guns21.security.logout:/logout}")
    private String logout;
    @Value("${com.guns21.security.username-parameter:username}")
    private String usernameParameter;
    @Value("${com.guns21.security.password-parameter:password}")
    private String passwordParameter;
    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private HttpLogoutSuccessHandler httpLogoutSuccessHandler;
    @Autowired
    private HttpAuthenticationFailureHandler httpAuthenticationFailureHandler;
    @Autowired
    private RedisIndexedSessionRepository redisOperationsSessionRepository;
    @Resource(name = "passwordAuthenticationProvider")
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private AuthExtValidator authExtValidator;

    @Bean
    @ConditionalOnMissingBean(name = "beforeLoginFilter")
    public Filter beforeLoginFilter() {
        return new AccessFilter();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new HttpAuthenticationSuccessHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "passwordAuthenticationProvider")
    public AuthenticationProvider passwordAuthenticationProvider(@Qualifier("passwordUserAuthService") UserAuthService userAuthService) {
        return new PasswordEncryptAuthenticationProvider(userAuthService);
    }

    @Bean
    public SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry() {
        return new SpringSessionBackedSessionRegistry((FindByIndexNameSessionRepository) this.redisOperationsSessionRepository);
    }

    /**
     * used to expose the AuthenticationManager
     * @return
     * @throws Exception
     */
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain authenticationSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //当有多个 HttpSecurity patterns 只能匹配Order优先级最好的HttpSecurity
                .authorizeHttpRequests(auth -> auth.requestMatchers(login, logout).permitAll()
                            .anyRequest().authenticated()
                )
                .addFilterBefore(beforeLoginFilter(), ChannelProcessingFilter.class)
                .addFilterBefore(new ValidatorFilter(authExtValidator), UsernamePasswordAuthenticationFilter.class)
                .formLogin(formLogin -> formLogin.loginPage(login)
                                .usernameParameter(usernameParameter)
                                .passwordParameter(passwordParameter)
                                .successHandler(authenticationSuccessHandler())
                                .failureHandler(httpAuthenticationFailureHandler)
                )
                .logout(fromLogout -> fromLogout.logoutUrl(logout)
                                .logoutSuccessHandler(httpLogoutSuccessHandler)
                                .invalidateHttpSession(true)
                )
                .csrf(csrf -> csrf.disable());

        //同一个账户多次登录限制，针对等是需要对之前的session进行表示
        httpSecurity
                .sessionManagement(sessionManagement ->
                        sessionManagement.maximumSessions(securityConfig.getMaximumSessions())
                                //.maxSessionsPreventsLogin(true)为true是多次登录时抛出异常
                                .sessionRegistry(springSessionBackedSessionRegistry())
                );

        return httpSecurity.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder("8iekd,a.oa0923.",1850,256, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA1);
        return pbkdf2PasswordEncoder;
    }

}
