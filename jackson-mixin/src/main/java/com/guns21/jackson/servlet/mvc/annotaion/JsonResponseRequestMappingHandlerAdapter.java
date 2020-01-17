package com.guns21.jackson.servlet.mvc.annotaion;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;

public class JsonResponseRequestMappingHandlerAdapter extends RequestMappingHandlerAdapter {
    private  static final Logger log = LoggerFactory.getLogger(JsonResponseRequestMappingHandlerAdapter.class);
    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        List<HandlerMethodReturnValueHandler> handlers = Lists.newArrayList(this.getReturnValueHandlers());

        decorateHandlers(handlers);
        this.setReturnValueHandlers(handlers);
    }

    private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                JsonResponseInjectingReturnValueHandler decorator = new JsonResponseInjectingReturnValueHandler(handler);
                int index = handlers.indexOf(handler);
                handlers.set(index, decorator);
                log.info("JsonResponse decorator support wired up");
                break;
            }
        }
    }
}
