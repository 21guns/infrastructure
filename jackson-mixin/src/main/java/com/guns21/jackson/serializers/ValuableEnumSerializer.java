package com.guns21.jackson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.guns21.common.enums.ValuableEnum;
import com.guns21.common.enums.ValuableEnumFactory;

import java.io.IOException;

public class ValuableEnumSerializer<E extends Enum<E> & ValuableEnum> extends StdSerializer<E> {

    private Class<E> clazz;

    public ValuableEnumSerializer(Class<E> vc) {
        super(vc);
        clazz = vc;
        ValuableEnumFactory.buildEnum(this.clazz);
    }


    @Override
    public void serialize(E e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(e.getValue());
    }
}
