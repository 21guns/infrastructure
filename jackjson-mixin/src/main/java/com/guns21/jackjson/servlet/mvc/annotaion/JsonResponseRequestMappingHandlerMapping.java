package com.guns21.jackjson.servlet.mvc.annotaion;

import com.guns21.jackjson.annotation.JsonMixin;
import com.guns21.jackjson.annotation.JsonResponse;
import com.guns21.jackjson.http.converter.json.ReadWriteMappingJackson2HttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Objects;

public class JsonResponseRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Autowired(required = false)
    protected ReadWriteMappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;


    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
        if (Objects.nonNull(mappingJackson2HttpMessageConverter)) {
            JsonResponse annotation = method.getAnnotation(JsonResponse.class);
            if (Objects.nonNull(annotation)) {
                for (JsonMixin jsonMixin : annotation.mixins()) {
                    mappingJackson2HttpMessageConverter.getWriteObjectMapper().addMixIn(jsonMixin.target(), jsonMixin.mixin());
                }
            }
        }
        super.registerHandlerMethod(handler, method, mapping);
    }
}
