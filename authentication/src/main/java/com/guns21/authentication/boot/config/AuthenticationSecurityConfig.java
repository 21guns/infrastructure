package com.guns21.authentication.boot.config;

import com.guns21.authentication.api.service.UserAuthService;
import com.guns21.authentication.ext.AuthExtValidator;
import com.guns21.authentication.filter.AccessFilter;
import com.guns21.authentication.filter.ValidatorFilter;
import com.guns21.authentication.security.handler.HttpAuthenticationFailureHandler;
import com.guns21.authentication.security.handler.HttpAuthenticationSuccessHandler;
import com.guns21.authentication.security.handler.HttpLogoutSuccessHandler;
import com.guns21.authentication.security.provider.PasswordEncryptAuthenticationProvider;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

/**
 * 认证
 * migration to  6.0
 * see https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 * see https://www.baeldung.com/spring-deprecated-websecurityconfigureradapter
 */
@Configuration
@EnableWebSecurity
public class AuthenticationSecurityConfig {

//    @Autowired
//    private AuthExtValidator authExtValidator;

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
    public LogoutSuccessHandler httpLogoutSuccessHandler() {
        return new HttpLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler httpAuthenticationFailureHandler(){
        return new HttpAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationProvider passwordAuthenticationProvider(@Qualifier("passwordUserAuthService") UserAuthService userAuthService,
                                                                 MessageSourceAccessor messageSourceAccessor,
                                                                 PasswordEncoder passwordEncoder) {
        return new PasswordEncryptAuthenticationProvider(messageSourceAccessor, userAuthService, passwordEncoder);
    }

//    @Bean
//    public RedisIndexedSessionRepository redisIndexedSessionRepository(RedisTemplate<String, Object> redisTemplate) {
//        return new RedisIndexedSessionRepository(redisTemplate);
//    }

    @Bean
    public SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry(RedisIndexedSessionRepository redisOperationsSessionRepository) {
        return new SpringSessionBackedSessionRegistry<>(redisOperationsSessionRepository);
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
    @Order(100)
    public SecurityFilterChain authenticationSecurityFilterChain(HttpSecurity httpSecurity, SecurityConfig securityConfig,
                                                                 SpringSessionBackedSessionRegistry springSessionBackedSessionRegistry) throws Exception {
        httpSecurity
                //当有多个 HttpSecurity patterns 只能匹配Order优先级最好的HttpSecurity
                .securityMatcher(securityConfig.getLogin(), securityConfig.getLogout())
//                .addFilterBefore(beforeLoginFilter(), ChannelProcessingFilter.class)
//                .addFilterBefore(new ValidatorFilter(authExtValidator), UsernamePasswordAuthenticationFilter.class)
                .formLogin(formLogin -> formLogin.loginProcessingUrl(securityConfig.getLogin())
                                .usernameParameter(securityConfig.getUsernameParameter())
                                .passwordParameter(securityConfig.getPasswordParameter())
                                .successHandler(authenticationSuccessHandler())
                                .failureHandler(httpAuthenticationFailureHandler())
                )
                .logout(fromLogout -> fromLogout.logoutUrl(securityConfig.getLogout())
                                .logoutSuccessHandler(httpLogoutSuccessHandler())
                                .invalidateHttpSession(true)
                )
                .csrf(AbstractHttpConfigurer::disable);

        //同一个账户多次登录限制，针对等是需要对之前的session进行表示
        httpSecurity
                .sessionManagement(sessionManagement ->
                        sessionManagement.maximumSessions(securityConfig.getMaximumSessions())
                                //.maxSessionsPreventsLogin(true)为true是多次登录时抛出异常
                                .sessionRegistry(springSessionBackedSessionRegistry)
                );

        return httpSecurity.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new Pbkdf2PasswordEncoder("8iekd,a.oa0923.",8,1850, 256);
    }

}
