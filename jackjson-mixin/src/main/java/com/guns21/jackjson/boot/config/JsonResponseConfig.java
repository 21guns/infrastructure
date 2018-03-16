package com.guns21.jackjson.boot.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.guns21.jackjson.datatype.StandrdLocalDateDeserializer;
import com.guns21.jackjson.datatype.StandrdLocalDateTimeDeserializer;
import com.guns21.jackjson.datatype.StandrdLocalTimeDeserializer;
import com.guns21.jackjson.http.converter.json.ReadWriteMappingJackson2HttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JsonResponseConfig {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) throws IllegalAccessException {
        MappingJackson2HttpMessageConverter jsonConverter = new ReadWriteMappingJackson2HttpMessageConverter(objectMapper);

        objectMapper.registerModule(new JavaTimeModule());
        return jsonConverter;
    }

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder
                .featuresToDisable(MapperFeature.DEFAULT_VIEW_INCLUSION)
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                //java 8 localDate
                .modules(new JavaTimeModule())
                .deserializerByType(LocalDateTime.class, localDateTimeDeserializer() )
                .deserializerByType(LocalDate.class, localDateDeserializer() )
                .deserializerByType(LocalTime.class, localTimeDeserializer() )
                .build();
    }

    @Bean
    public LocalDateTimeDeserializer localDateTimeDeserializer() {
       return new StandrdLocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Bean
    public LocalDateDeserializer localDateDeserializer() {
        return new StandrdLocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Bean
    public LocalTimeDeserializer localTimeDeserializer() {
        return new StandrdLocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}