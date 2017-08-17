package com.guns21.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gy on 2016/2/4.
 */
@Target( {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthAnnotation {
  String value() default "";
}
