package com.guns21.jackson.servlet.mvc.annotaion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guns21.jackson.JaskonMixinCache;
import com.guns21.jackson.annotation.JsonMixin;
import com.guns21.jackson.annotation.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Objects;

public class JsonResponseRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    private  static final Logger log = LoggerFactory.getLogger(JsonResponseRequestMappingHandlerMapping.class);

    private ObjectMapper objectMapper;

    public JsonResponseRequestMappingHandlerMapping(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
        if (Objects.nonNull(objectMapper)) {
            JsonResponse annotation = method.getAnnotation(JsonResponse.class);
            if (Objects.nonNull(annotation)) {
                ObjectMapper copy = objectMapper.copy();
                for (JsonMixin jsonMixin : annotation.mixins()) {
                    copy.addMixIn(jsonMixin.target(), jsonMixin.mixin());
                }
                log.info("Add mixin for mapping {} onto {} ", mapping, method);
                JaskonMixinCache.put(method.toString(),copy);
            }
        }
        super.registerHandlerMethod(handler, method, mapping);
    }
}
