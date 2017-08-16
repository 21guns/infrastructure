package com.guns21.event.annotation;

import com.guns21.event.EventBusClient;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Created by jliu on 2017/6/8.
 */
@StreamListener
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EventBusListener {
    @AliasFor(annotation=StreamListener.class, attribute="target")
    String value() default EventBusClient.INPUT;

    @AliasFor(annotation=StreamListener.class, attribute="value")
    String target() default EventBusClient.INPUT;

    @AliasFor(annotation=StreamListener.class, attribute="condition")
    String eventType() default "";
}
