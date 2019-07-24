package com.guns21.feign.annotation;

import java.lang.annotation.*;

/**
 * Created by Liu Xiang on 2019-07-19.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface FeignService {

    /**
     * Set base URL
     */
    String value();
}
