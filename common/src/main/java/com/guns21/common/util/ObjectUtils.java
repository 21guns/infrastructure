package com.guns21.common.util;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jliu on 2016/11/23.
 */
public class ObjectUtils {
    public static <T> T defaultIfNull(T object, T defaultValue) {
        return object != null ? object : defaultValue;
    }

    public static boolean nonEmpty(Collection collection) {
        return collection != null && collection.size() > 0;
    }

    public static boolean isEmpty(Collection collection) {
        return !nonEmpty(collection);
    }

    public static boolean nonEmpty(Map map) {
        return map != null && map.size() > 0;
    }

    public static boolean isEmpty(Map map) {
        return !nonEmpty(map);
    }

    public static boolean hasText(String str) {
        return Objects.nonNull(str) && str.trim().length() > 0;
    }

}

