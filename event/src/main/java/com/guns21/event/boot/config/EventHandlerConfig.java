package com.guns21.event.boot.config;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binding.StreamListenerAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.cloud.stream.config.BindingServiceConfiguration.STREAM_LISTENER_ANNOTATION_BEAN_POST_PROCESSOR_NAME;

@Configuration
public class EventHandlerConfig {
 
    /*
     * The SpEL expression used to allow the Spring Cloud Stream Binder to dispatch to methods
     * Annotated with @EventHandler
     */
 
    private String eventHandlerSpelPattern = "headers['eventType']=='%s'";
 
    /**
     * Override the default {@link StreamListenerAnnotationBeanPostProcessor} to inject value of
     * 'eventType' attribute into 'condition' expression.
     * @return
     */
    @Bean(name = STREAM_LISTENER_ANNOTATION_BEAN_POST_PROCESSOR_NAME)
    public StreamListenerAnnotationBeanPostProcessor streamListenerAnnotationBeanPostProcessor() {
       return new StreamListenerAnnotationBeanPostProcessor() {
        @Override
        protected StreamListener postProcessAnnotation(StreamListener originalAnnotation, Method annotatedMethod) {
           Map<String, Object> attributes = new HashMap<>(
          AnnotationUtils.getAnnotationAttributes(originalAnnotation));
          if (StringUtils.hasText(originalAnnotation.condition())) {
             String spelExpression = String.format(eventHandlerSpelPattern, originalAnnotation.condition());
             attributes.put("condition", spelExpression);
          }
        return AnnotationUtils.synthesizeAnnotation(attributes, StreamListener.class, annotatedMethod);
      }
       };
    }
}