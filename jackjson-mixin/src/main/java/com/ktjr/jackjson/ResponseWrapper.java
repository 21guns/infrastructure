package com.ktjr.jackjson;

import com.ktjr.jackjson.annotation.JsonResponse;

interface ResponseWrapper {

    boolean hasJsonMixins();

    JsonResponse getJsonResponse();

    Object getOriginalResponse();

}
