package com.guns21.jackson.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JsonMixin {
    public Class<?> target();

    public Class<?> mixin();
}
