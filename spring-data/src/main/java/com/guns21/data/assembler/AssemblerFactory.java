package com.guns21.data.assembler;

import com.guns21.common.util.ObjectUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
        if (Objects.isNull(source)) {
            return null;
        }
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

    /**
     *
     * @param source 原对象
     * @param target 目标对象
     * @param <T>
     * @return
     */
    public static <T> T to(Object source, T target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            return null;
        }
        copyProperties(source, target);
        return target;
    }

    /**
     *
     * @param source 原对象
     * @param targetClass 目标类，需要有无参构造方法
     * @param <T>
     * @return
     */
    public static <T> Optional<T> toOptional(Object source, T target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            return Optional.empty();
        }
        copyProperties(source, target);
        return Optional.of(target);
    }

    /**
     * 转换list中对象到指定类，会过滤掉null对象
     *
     * @param list 源list对象
     * @param targetClass 目标类
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T,S> List<T> toList(List<S> list, Class<T> targetClass ) {
        if (ObjectUtils.nonEmpty(list)) {
            return list.stream()
                    .filter(s -> Objects.nonNull(s))
                    .map(source -> to(source, targetClass))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
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
