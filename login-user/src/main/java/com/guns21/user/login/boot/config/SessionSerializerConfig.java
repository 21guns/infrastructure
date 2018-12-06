package com.guns21.user.login.boot.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guns21.user.login.mixin.AuthenticationJasksonModue;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SessionSerializerConfig implements BeanClassLoaderAware {

    private List<SpringSessionRedisSerializerObjectMapperConfigure> configurers = new ArrayList<>();
    private ClassLoader loader;

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        ObjectMapper objectMapper = objectMapper();
        for (SpringSessionRedisSerializerObjectMapperConfigure configurer : configurers) {
            configurer.configureObjectMapper(objectMapper);
        }
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }


    /**
     * Customized {@link ObjectMapper} to add mix-in for class that doesn't have default
     * constructors
     *
     * @return the {@link ObjectMapper} to use
     */
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(SecurityJackson2Modules.getModules(this.loader));
        mapper.registerModule(new AuthenticationJasksonModue());
        //use set property with NoArgsConstructor
        //@see org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        return mapper;
    }

    @Autowired(required = false)
    public void setConfigurers(List<SpringSessionRedisSerializerObjectMapperConfigure> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers.addAll(configurers);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.beans.factory.BeanClassLoaderAware#setBeanClassLoader(java.lang
     * .ClassLoader)
     */
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.loader = classLoader;
    }


}
