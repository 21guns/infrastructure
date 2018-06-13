package com.guns21.data.assembler;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

public class AssemblerFactory {

    private static final Map<String, BeanCopier> beanCopierCache = new ConcurrentHashMap<String, BeanCopier>(64);

    /**
     *
     * @param source 原对象
     * @param targetClass 目标类，需要有无参构造方法
     * @param <T>
     * @return
     */
    public static <T> T to(Object source, Class<T> targetClass) {
        Objects.requireNonNull(source,"source is null");
        return toAnother(source, targetClass);
    }

    /**
     *
     * @param source 原对象
     * @param targetClass 目标类，需要有无参构造方法
     * @param <T>
     * @return
     */
    public static <T> Optional<T> toOptional(Object source, Class<T> targetClass) {
        if (Objects.isNull(source) ) {
            return Optional.empty();
        }
        return Optional.of(toAnother(source, targetClass));
    }

    private static <T> T toAnother(Object source, Class<T> targetClass) {
        T t = null;
        try {
            t = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(format("Create new instance of %s failed: %s", targetClass, e.getMessage()));
        }
        copyProperties(source, t);
        return t;
    }
    private static void copyProperties(Object source, Object target) {
        BeanCopier copier = getBeanCopier(source.getClass(), target.getClass());
        copier.copy(source, target, null);
    }

    private static BeanCopier getBeanCopier(Class sourceClass, Class targetClass) {
        String beanKey = generateKey(sourceClass, targetClass);
        BeanCopier copier = null;
        if (!beanCopierCache.containsKey(beanKey)) {
            copier = BeanCopier.create(sourceClass, targetClass, false);
            beanCopierCache.put(beanKey, copier);
        } else {
            copier = beanCopierCache.get(beanKey);
        }
        return copier;
    }

    private static String generateKey(Class<?> sourceClass, Class<?> targetClass) {
        return sourceClass.toString() + "->" + targetClass.toString();
    }

}
