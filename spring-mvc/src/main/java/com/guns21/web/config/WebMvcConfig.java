package com.guns21.web.config;

import com.guns21.web.converter.StringToDateConvert;
import com.guns21.web.method.resolver.CurrentUserMethodArgumentResolver;
import com.guns21.web.method.resolver.RequireUuidMethodArgumentResolver;
import com.guns21.web.servlet.AuthTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    super.addInterceptors(registry);
    registry.addInterceptor(new AuthTokenInterceptor()).addPathPatterns("/app/**");
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    super.addArgumentResolvers(argumentResolvers);
    argumentResolvers.add(new CurrentUserMethodArgumentResolver());
    argumentResolvers.add(new RequireUuidMethodArgumentResolver());
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    super.addFormatters(registry);
    registry.addConverter(new StringToDateConvert());
  }

}