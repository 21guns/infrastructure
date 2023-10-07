package com.guns21.jackson.boot.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.google.common.collect.Sets;
import com.guns21.common.enums.ValuableEnum;
import com.guns21.common.util.ObjectUtils;
import com.guns21.jackson.annotation.JsonOnlyProperties;
import com.guns21.jackson.datatype.deser.StandrdLocalDateDeserializer;
import com.guns21.jackson.datatype.deser.StandrdLocalDateTimeDeserializer;
import com.guns21.jackson.datatype.deser.StandrdLocalTimeDeserializer;
import com.guns21.jackson.datatype.ser.StandrdLocalDateSerializer;
import com.guns21.jackson.datatype.ser.StandrdLocalDateTimeSerializer;
import com.guns21.jackson.datatype.ser.StandrdLocalTimeSerializer;
import com.guns21.jackson.deserializer.ValuableEnumDeserializer;
import com.guns21.jackson.http.converter.json.JsonResponseAwareJsonMessageConverter;
import com.guns21.jackson.serializers.ValuableEnumSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Configuration
public class JsonResponseConfig {
    private static final Logger logger = LoggerFactory.getLogger(JsonResponseConfig.class);

    private static final DateTimeFormatter LOCAL_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter LOCAL_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter LOCAL_TIME = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private List<JacksonObjectMapperBuilderConfigure> configurers = new ArrayList<JacksonObjectMapperBuilderConfigure>();

    @Value("${com.guns21.spring.mvc.valuable-enum-package:#{null}}")
    private String valuableEnumPackage;

    @Autowired(required = false)
    public void setConfigurers(List<JacksonObjectMapperBuilderConfigure> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers.addAll(configurers);
        }
    }
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(@Qualifier("jacksonObjectMapper") ObjectMapper objectMapper) throws IllegalAccessException {
        MappingJackson2HttpMessageConverter jsonConverter = new JsonResponseAwareJsonMessageConverter(objectMapper);

//        objectMapper.registerModule(new JavaTimeModule());
        return jsonConverter;
    }

    private void registerEnumConverter(ObjectMapper objectMapper) {
        if (!ObjectUtils.hasText(valuableEnumPackage)) {
            return;
        }
        SimpleModule module = new SimpleModule();

        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);

        // Add include filters which matches all the classes (or use your own)
        provider.addIncludeFilter(new AssignableTypeFilter(ValuableEnum.class));

        // Aet matching classes defined in the package
        final Set<BeanDefinition> enumClasses = provider.findCandidateComponents(valuableEnumPackage);

        enumClasses.forEach(beanDefinition -> {
            try {
                Class<Object> clazz = (Class<Object>) Class.forName(beanDefinition.getBeanClassName());
                if (ValuableEnum.class.isAssignableFrom(clazz) && Enum.class.isAssignableFrom(clazz)) {
                    module.addDeserializer(clazz, new ValuableEnumDeserializer(clazz));
                    module.addSerializer(clazz, new ValuableEnumSerializer(clazz));
                }
            } catch (ClassNotFoundException e) {
                logger.error("class not found", e);
            }
        });

        objectMapper.registerModule(module);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "jacksonObjectMapper")
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        builder
                .featuresToDisable(MapperFeature.DEFAULT_VIEW_INCLUSION)
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                //java 8 localDate,jdk8
                .modules(new JavaTimeModule(), new Jdk8Module())
                .deserializerByType(LocalDateTime.class, localDateTimeDeserializer())
                .deserializerByType(LocalDate.class, localDateDeserializer())
                .deserializerByType(LocalTime.class, localTimeDeserializer())
                .serializerByType(LocalDateTime.class, localDateTimeSerializer())
                .serializerByType(LocalDate.class, localDateSerializer())
                .serializerByType(LocalTime.class, localTimeSerializer())

                .annotationIntrospector(new JacksonAnnotationIntrospector(){

                    @Override
                    public JsonIgnoreProperties.Value findPropertyIgnorals(Annotated ac) {
                        JsonOnlyProperties annotation = ac.getAnnotation(JsonOnlyProperties.class);
                        if (Objects.isNull(annotation)) {
                            return super.findPropertyIgnorals(ac);

                        }
                        if (ac instanceof AnnotatedClass) {
                            AnnotatedClass annotatedClass = (AnnotatedClass) ac;
                            Set<String> allFieldName = StreamSupport.stream(annotatedClass.fields().spliterator(), false)
                                    .map(AnnotatedField::getName)
                                    .collect(Collectors.toSet());

                            Set<String> onlyFieldName = Sets.newHashSet(annotation.value());
                            allFieldName.removeAll(onlyFieldName);
                            return JsonIgnoreProperties.Value.forIgnoredProperties(allFieldName);

                        }
                        return super.findPropertyIgnorals(ac);
                    }

                })
        ;

        for (JacksonObjectMapperBuilderConfigure configurer : configurers) {
            configurer.configureObjectMapper(builder);
        }

        ObjectMapper objectMapper = builder.build();
        registerEnumConverter(objectMapper);

        return objectMapper;
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