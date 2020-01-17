package com.guns21.jackson.wrapper;


import com.guns21.jackson.annotation.JsonResponse;

public interface ResponseWrapper {

    boolean hasJsonMixins();

    JsonResponse getJsonResponse();

    Object getOriginalResponse();

    String getMethodName();
}
