package com.guns21.web.boot.config;

import com.guns21.web.method.resolver.RequireUuidMethodArgumentResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 1.implements WebMvcRegistrations spring boot 2 support
 * 2.extend WebMvcConfigurerAdapter spring boot < 2
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AuthTokenInterceptor()).addPathPatterns("/app/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new RequireUuidMethodArgumentResolver());
    }

//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new StringToDateConvert());
//    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }


}