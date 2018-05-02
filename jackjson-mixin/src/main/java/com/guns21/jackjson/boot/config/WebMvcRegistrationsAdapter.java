package com.guns21.jackjson.boot.config;

import com.guns21.jackjson.servlet.mvc.annotaion.JsonResponseRequestMappingHandlerMapping;
import com.guns21.jackjson.servlet.mvc.annotaion.JsonResponseRequestMappingHandlerAdapter;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

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
