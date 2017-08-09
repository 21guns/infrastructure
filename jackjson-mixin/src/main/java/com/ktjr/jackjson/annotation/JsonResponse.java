package com.ktjr.jackjson.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * see:https://github.com/jackmatt2/JsonResponse
 * 使用mixins进行json属性过滤
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonResponse {
    public JsonMixin[] mixins();
}
