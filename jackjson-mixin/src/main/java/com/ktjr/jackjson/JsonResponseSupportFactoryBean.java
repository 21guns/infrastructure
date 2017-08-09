package com.ktjr.jackjson;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;

/**
 * Modified Spring internal Return value handlers, and wires up a decorator to
 * add support for @JsonResponseView
 * 
 * @author Jack Matthews
 * 
 */
public final class JsonResponseSupportFactoryBean implements InitializingBean {

    Logger log = LoggerFactory.getLogger(JsonResponseSupportFactoryBean.class);

    @Autowired
    @Qualifier("requestMappingHandlerAdapter")
    private RequestMappingHandlerAdapter adapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        // Spring 4 way
        List<HandlerMethodReturnValueHandler> handlers = Lists.newArrayList(this.adapter.getReturnValueHandlers());

        decorateHandlers(handlers);
        this.adapter.setReturnValueHandlers(handlers);
    }

    private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                JsonResponseInjectingReturnValueHandler decorator = new JsonResponseInjectingReturnValueHandler(handler);
                int index = handlers.indexOf(handler);
                handlers.set(index, decorator);
                this.log.info("JsonResponse decorator support wired up");
                break;
            }
        }
    }

}