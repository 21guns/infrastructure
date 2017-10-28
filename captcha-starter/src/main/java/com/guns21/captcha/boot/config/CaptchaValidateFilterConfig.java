package com.guns21.captcha.boot.config;

import com.guns21.result.domain.Result;
import com.guns21.servlet.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 校验验证码正确性，根据登录的url
 * see https://stackoverflow.com/questions/34314084/howto-additionally-add-spring-security-captcha-filter-for-specific-urls-only
 */
@Configuration
@ConfigurationProperties(prefix = "com.guns21.captcha")
public class CaptchaValidateFilterConfig {

    private String[] validate = {"/login"};

    @Bean
    public BeforeLoginFilter beforeLoginFilter() {
        return new BeforeLoginFilter();
    }


    @Bean
    public FilterRegistrationBean beforeLoginFilterRegistration(BeforeLoginFilter beforeLoginFilter) {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(beforeLoginFilter);
        registration.addUrlPatterns(validate);
        registration.setName("beforeLoginFilter");
        registration.setOrder(10);
        return registration;
    }

    public void setValidate(String[] validate) {
        this.validate = validate;
    }


    public class BeforeLoginFilter implements Filter {

        @Autowired
        private RedisTemplate<String, String> template;

        private List<RequestMatcher> requestMatchers = new ArrayList<RequestMatcher>();;

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            for (String s : validate) {
                requestMatchers.add(new AntPathRequestMatcher(s));
            }
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
                throws IOException, ServletException {

            //任何不匹配validate url的请求都不进行验证码校验
            boolean anyMatch = requestMatchers.stream().anyMatch(requestMatcher -> requestMatcher.matches((HttpServletRequest) request));
            if (!anyMatch) {
                filterChain.doFilter(request, response);
                return;
            }
            String qCaptcha = request.getParameter("captcha");
            if (StringUtils.hasText(qCaptcha)) {
                String captcha = template.opsForValue().get(CaptchaServletConfig.KEY_PREFIX + qCaptcha);
                if (StringUtils.hasText(captcha)) {
                    if (captcha.equals(qCaptcha)) {
                        filterChain.doFilter(request, response);
                    } else {
                        // !captcha.equals(qCaptcha) 验证码输入错误
                        ResponseUtils.writeResponse(response, Result.fail("验证码错误"));
                    }
                } else {
                    // captcha == null 验证码过期
                    ResponseUtils.writeResponse(response, Result.fail("验证码错误"));
                }
            } else {
                // qCaptcha == null 未输入验证码
                ResponseUtils.writeResponse(response, Result.fail("未输入验证码"));

            }
            return;
        }

        @Override
        public void destroy() {
        }

    }
}
