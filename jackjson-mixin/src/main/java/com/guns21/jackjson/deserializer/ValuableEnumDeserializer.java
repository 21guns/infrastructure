package com.guns21.jackjson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.guns21.common.enums.ValuableEnum;
import com.guns21.common.enums.ValuableEnumFactory;

import java.io.IOException;

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
    public E deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Byte v = p.readValueAs(Byte.class);
        return ValuableEnumFactory.getEnum(clazz, v);
    }
}
