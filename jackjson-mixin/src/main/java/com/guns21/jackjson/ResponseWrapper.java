package com.guns21.jackjson;

import com.guns21.jackjson.annotation.JsonResponse;

interface ResponseWrapper {

  boolean hasJsonMixins();

  JsonResponse getJsonResponse();

  Object getOriginalResponse();

}
