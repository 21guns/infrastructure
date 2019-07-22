package com.guns21.feign.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Created by Liu Xiang on 2019-07-19.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface FeignService {

    @AliasFor("urlPrefix")
    String value();

    String urlPrefix();
}
