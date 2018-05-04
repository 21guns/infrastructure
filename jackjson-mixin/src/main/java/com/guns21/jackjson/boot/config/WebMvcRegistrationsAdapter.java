package com.guns21.jackjson.boot.config;

import com.guns21.jackjson.servlet.mvc.annotaion.JsonResponseRequestMappingHandlerAdapter;
import com.guns21.jackjson.servlet.mvc.annotaion.JsonResponseRequestMappingHandlerMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 1.implements WebMvcRegistrations spring boot 2 support
 * 2.extend WebMvcConfigurationSupport spring boot < 2
 */
@Configuration
public class WebMvcRegistrationsAdapter implements WebMvcRegistrations {

    /**
     * RequestMappingHandlerMapping 用来映射controller中的method的各个属性
     * @return
     */
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new JsonResponseRequestMappingHandlerMapping();
    }

    @Override
    public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
        return new JsonResponseRequestMappingHandlerAdapter();
    }
}
