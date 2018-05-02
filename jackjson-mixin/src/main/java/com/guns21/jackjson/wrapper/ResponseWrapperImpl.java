package com.guns21.jackjson.wrapper;


import com.guns21.jackjson.annotation.JsonResponse;

final public class ResponseWrapperImpl implements ResponseWrapper {

    private final Object originalResponse;
    private final JsonResponse jsonResponse;
    private final String methodName;

    public ResponseWrapperImpl(Object originalResponse, JsonResponse jsonResponse, String methodName) {
        this.originalResponse = originalResponse;
        this.jsonResponse = jsonResponse;
        this.methodName = methodName;
    }

    @Override
    public boolean hasJsonMixins() {
        return this.jsonResponse.mixins().length > 0;
    }

    @Override
    public JsonResponse getJsonResponse() {
        return this.jsonResponse;
    }

    @Override
    public Object getOriginalResponse() {
        return this.originalResponse;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }
}
