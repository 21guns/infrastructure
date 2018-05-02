package com.guns21.jackjson.wrapper;


import com.guns21.jackjson.annotation.JsonResponse;

public interface ResponseWrapper {

    boolean hasJsonMixins();

    JsonResponse getJsonResponse();

    Object getOriginalResponse();

    String getMethodName();
}
