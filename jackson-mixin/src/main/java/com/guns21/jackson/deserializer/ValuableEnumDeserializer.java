package com.guns21.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.guns21.common.enums.ValuableEnum;
import com.guns21.common.enums.ValuableEnumFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by Liu Xiang on 2018/8/2.
 *
 * @param <E> Enum extends ValuableEnum interface
 */
public class ValuableEnumDeserializer<E extends Enum<E> & ValuableEnum> extends StdDeserializer<E> {

    private Class<E> clazz;

    public ValuableEnumDeserializer(Class<E> vc) {
        super(vc);
        clazz = vc;
        ValuableEnumFactory.buildEnum(this.clazz);
    }

    @Override
    public E deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Byte v = p.readValueAs(Byte.class);
        if (Objects.isNull(v)) {
            return null;
        }

        return ValuableEnumFactory.getEnum(clazz, v);
    }
}
