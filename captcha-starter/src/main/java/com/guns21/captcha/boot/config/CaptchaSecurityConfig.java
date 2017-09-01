package com.guns21.captcha.boot.config;

import com.guns21.result.domain.Result;
import com.guns21.servlet.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.*;

/**
 * 校验验证码正确性，根据登录的url
 * see https://stackoverflow.com/questions/34314084/howto-additionally-add-spring-security-captcha-filter-for-specific-urls-only
 */
@Configuration
@EnableWebSecurity
@Order(1)
@ConfigurationProperties(prefix = "com.guns21.captcha")
public class CaptchaSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RedisTemplate<String, String> template;

    private String[] validate = {"/login"};
    /**
     * 定义web的访问权限
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .requestMatchers().antMatchers(validate)
                .and()
                .addFilterAt(new Filter() {
                    @Override
                    public void init(FilterConfig filterConfig) throws ServletException {

                    }

                    @Override
                    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

                        String qCaptcha = request.getParameter("captcha");
                        // 1.qCaptcha == null 未输入验证码
                        if (!StringUtils.hasText(qCaptcha)) {
                            ResponseUtils.writeResponse(response, Result.fail("未输入验证码"));
                            return;
                        }

                        // 2.captcha == null 验证码过期
                        String captcha = template.opsForValue().get(CaptchaServletConfig.KEY_PREFIX + qCaptcha);
                        if (!StringUtils.hasText(captcha)) {
                            ResponseUtils.writeResponse(response, Result.fail("验证码过期"));
                            return;
                        }

                        // 3.!captcha.equals(qCaptcha) 验证码输入错误
                        if (!captcha.equals(qCaptcha)) {
                            ResponseUtils.writeResponse(response, Result.fail("验证码输入错误"));
                            return;
                        }
                        filterChain.doFilter(request, response);
                    }

                    @Override
                    public void destroy() {

                    }
                }, (Class<? extends Filter>) ChannelProcessingFilter.class)
                .csrf().disable();
//                .authorizeRequests()
//                .anyRequest().authenticated();
    }


}
