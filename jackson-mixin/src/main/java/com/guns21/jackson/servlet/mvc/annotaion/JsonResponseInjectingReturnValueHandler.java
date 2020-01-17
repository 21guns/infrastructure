package com.guns21.jackson.servlet.mvc.annotaion;

import com.guns21.jackson.annotation.JsonResponse;
import com.guns21.jackson.wrapper.ResponseWrapperImpl;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Decorator that detects a declared {@link JsonResponse}, and injects support
 * if required
 * 
 * @author Jack Matthews
 * 
 */
public final class JsonResponseInjectingReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler delegate;

    public JsonResponseInjectingReturnValueHandler(HandlerMethodReturnValueHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return this.delegate.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {

        JsonResponse jsonResponse = returnType.getMethodAnnotation(JsonResponse.class);
        if (jsonResponse != null) {
            returnValue = new ResponseWrapperImpl(returnValue, jsonResponse, returnType.getMethod().toString());
        }

        this.delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }
}