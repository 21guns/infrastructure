package com.guns21.feign.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guns21.feign.annotation.FeignService;
import com.guns21.feign.codec.ResultDecoder;
import com.guns21.feign.target.SpringSessionHeaderTokenTarget;
import feign.Feign;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * Created by Liu Xiang on 2019-07-22.
 */
public class FeignServiceFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware, EnvironmentAware {
    ApplicationContext applicationContext;

    Environment environment;

    private Class<T> feignServiceInterface;

    public FeignServiceFactoryBean() {}

    public FeignServiceFactoryBean(Class<T> feignServiceInterface) {
        this.feignServiceInterface = feignServiceInterface;
    }

    @Override
    public T getObject() throws Exception {
        FeignService annotation = feignServiceInterface.getAnnotation(FeignService.class);
        String urlPrefix = annotation.value();
        if (urlPrefix.matches("^\\$\\{.+\\}$")) {
            urlPrefix = environment.getProperty(urlPrefix.replaceAll("^\\$\\{|\\}$", ""));
        }
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
        return Feign.builder()
                .decode404()
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new ResultDecoder(objectMapper))
                .target(SpringSessionHeaderTokenTarget.newTarget(this.feignServiceInterface, urlPrefix));
    }

    @Override
    public Class<?> getObjectType() {
        return this.feignServiceInterface;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
