package com.guns21.jackson.boot.config;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public interface JacksonObjectMapperBuilderConfigure {

    default void configureObjectMapper(Jackson2ObjectMapperBuilder mapperBuilder){}
}
