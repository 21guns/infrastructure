package com.guns21.jackjson.boot.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.guns21.jackjson.datatype.deser.StandrdLocalDateDeserializer;
import com.guns21.jackjson.datatype.deser.StandrdLocalDateTimeDeserializer;
import com.guns21.jackjson.datatype.deser.StandrdLocalTimeDeserializer;
import com.guns21.jackjson.datatype.ser.StandrdLocalDateSerializer;
import com.guns21.jackjson.datatype.ser.StandrdLocalDateTimeSerializer;
import com.guns21.jackjson.datatype.ser.StandrdLocalTimeSerializer;
import com.guns21.jackjson.http.converter.json.JsonResponseAwareJsonMessageConverter;
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

    private static final DateTimeFormatter LOCAL_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter LOCAL_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter LOCAL_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) throws IllegalAccessException {
        MappingJackson2HttpMessageConverter jsonConverter = new JsonResponseAwareJsonMessageConverter(objectMapper);

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
                .serializerByType(LocalDateTime.class, localDateTimeSerializer() )
                .serializerByType(LocalDate.class, localDateSerializer() )
                .serializerByType(LocalTime.class, localTimeSerializer() )
                .build();
    }

    @Bean
    public LocalDateTimeDeserializer localDateTimeDeserializer() {
       return new StandrdLocalDateTimeDeserializer(LOCAL_DATE_TIME);
    }

    @Bean
    public LocalDateDeserializer localDateDeserializer() {
        return new StandrdLocalDateDeserializer(LOCAL_DATE);
    }

    @Bean
    public LocalTimeDeserializer localTimeDeserializer() {
        return new StandrdLocalTimeDeserializer(LOCAL_TIME);
    }


    @Bean
    public LocalDateTimeSerializer localDateTimeSerializer() {
        return new StandrdLocalDateTimeSerializer(LOCAL_DATE_TIME);
    }

    @Bean
    public LocalDateSerializer localDateSerializer() {
        return new StandrdLocalDateSerializer(LOCAL_DATE);
    }

    @Bean
    public LocalTimeSerializer localTimeSerializer() {
        return new StandrdLocalTimeSerializer(LOCAL_TIME);
    }
}