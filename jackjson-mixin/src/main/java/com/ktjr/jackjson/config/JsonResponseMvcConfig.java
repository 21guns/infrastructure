package com.ktjr.jackjson.config;

import com.ktjr.jackjson.JsonResponseAwareJsonMessageConverter;
import com.ktjr.jackjson.JsonResponseSupportFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class JsonResponseMvcConfig extends WebMvcConfigurerAdapter {

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    super.configureMessageConverters(converters);
    converters.add(new JsonResponseAwareJsonMessageConverter());
  }

  @Bean
  public JsonResponseSupportFactoryBean jsonResponseSupportFactoryBean() {
    return new JsonResponseSupportFactoryBean();
  }
}