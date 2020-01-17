package com.guns21.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final public class JacksonMixinCache {

    private static final Map<String, ObjectMapper> JACKSON_MIXIN_CACHE = new ConcurrentHashMap<String, ObjectMapper>(64);

    public static ObjectMapper put(String key, ObjectMapper objectMapper) {
        return JACKSON_MIXIN_CACHE.put(key, objectMapper);
    }

    public static ObjectMapper get(String key) {
        return JACKSON_MIXIN_CACHE.get(key);
    }
}
