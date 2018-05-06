package com.guns21.user.login.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface SpringSessionRedisSerializerObjectMapperConfigure {

    default void configureObjectMapper(ObjectMapper objectMapper){}
}
