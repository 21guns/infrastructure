package com.guns21.web.formatter;

import com.guns21.common.enums.ValuableEnum;
import com.guns21.common.enums.ValuableEnumFactory;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class ValuableEnumFormatter<E extends Enum<E> & ValuableEnum> implements Formatter<E> {

    private Class<E> clazz;

    public ValuableEnumFormatter(Class<?> clazz) {
        this.clazz = (Class<E>) clazz;
        ValuableEnumFactory.buildEnum(this.clazz);
    }

    @Override
    public E parse(String text, Locale locale) throws ParseException {
        return ValuableEnumFactory.getEnum(clazz, Byte.parseByte(text));
    }

    @Override
    public String print(E object, Locale locale) {
        return object.getValue().toString();
    }
}
