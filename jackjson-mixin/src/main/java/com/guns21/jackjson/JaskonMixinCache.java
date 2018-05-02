package com.guns21.jackjson;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final public class JaskonMixinCache {

    private static final Map<String, ObjectMapper> jasksonMixinCache = new ConcurrentHashMap<String, ObjectMapper>(64);

    public static ObjectMapper put(String key, ObjectMapper objectMapper) {
        return jasksonMixinCache.put(key, objectMapper);
    }

    public static ObjectMapper get(String key) {
        return jasksonMixinCache.get(key);
    }
}
