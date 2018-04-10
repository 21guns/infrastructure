package com.guns21.assembler;

import org.springframework.cglib.beans.BeanCopier;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class AssemblerFactory {

    private static Map<Class, ClassBeanCopierMap>  DO_MAP = new HashMap<>();
    private static Map<Class, ClassBeanCopierMap>  DTO_MAP = new HashMap<>();

    public static <DTO, DO> DO fromDTO(DTO dto, Supplier<DO> supplier) {
        if (Objects.isNull(dto)) {
            return null;
        }
        DO ddo = supplier.get();
        BeanCopier beanCopier = DTO_MAP.get(ddo.getClass()).beanCopier;
        beanCopier.copy(dto, ddo, null);
        return ddo;
    }

    public static <DO, DTO>  DTO toDTO(DO userDO, Supplier<DTO> supplier) {
        if (Objects.isNull(userDO)) {
            return null;
        }
        DTO dto = supplier.get();
//        toCopier.copy(userDO, userDTO, null);
        return dto;
    }

    public class ClassBeanCopierMap {
        private Class dto;
        private Class ddo;
        private BeanCopier beanCopier;
    }
}
