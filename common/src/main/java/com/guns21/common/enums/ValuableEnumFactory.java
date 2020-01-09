package com.guns21.common.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ValuableEnumFactory {

    private static final Map<Class, Map<Byte, Enum>> CACHE = new HashMap<>();

    public static <E extends Enum<E> & ValuableEnum> void buildEnum(Class<E> clazz) {
        if (Objects.isNull(clazz)
            || !(ValuableEnum.class.isAssignableFrom(clazz) && Enum.class.isAssignableFrom(clazz))) {
            return;
        }
        Map<Byte, Enum> map = CACHE.get(clazz);
        if (Objects.isNull(map)) {
            E[] values = clazz.getEnumConstants();
            if (Objects.isNull(values)) return;
            map = CACHE.computeIfAbsent(clazz, k -> new HashMap<>());
            for (E value : values) {
                map.put(value.getValue(), value);
            }
        }

    }

    public static <E extends Enum<E> & ValuableEnum> E getEnum(Class<E> clazz, byte rawValue) {
        Map<Byte, Enum> cache = CACHE.get(clazz);
        if (Objects.nonNull(cache)) {
            return (E) cache.get(rawValue);
        }
        return null;
    }
}
