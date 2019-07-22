package com.guns21.feign.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guns21.data.domain.result.MessageResult;
import feign.Response;
import feign.jackson.JacksonDecoder;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Liu Xiang on 2019-07-19.
 */
public class ResultDecoder extends JacksonDecoder {
    public ResultDecoder(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        Type shadeType = type;

        Boolean returnOptionalOrStream = Stream.of(Optional.class, Stream.class).anyMatch(c -> {
            if (type instanceof ParameterizedType) {
                return Objects.equals(c, ((ParameterizedType) type).getRawType());
            } else {
                return Objects.equals(c, type);
            }
        });

        if (returnOptionalOrStream) {
            if (type instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) type;
                if (Objects.equals(Stream.class, pt.getRawType())) {
                    shadeType = ParameterizedTypeImpl.make(MessageResult.class,
                            new Type[]{ ParameterizedTypeImpl.make(ArrayList.class, pt.getActualTypeArguments(), null)}, null);
                } else {
                    shadeType = ParameterizedTypeImpl.make(MessageResult.class, pt.getActualTypeArguments(), null);
                }
            } else {
                shadeType = MessageResult.class;
            }
        }

        Object result = super.decode(response, shadeType);
        if (result instanceof MessageResult) {
            MessageResult messageResult = (MessageResult) result;
            if (type.getTypeName().startsWith(Optional.class.getTypeName())) {
                return messageResult.optional();
            }

            if (type.getTypeName().startsWith(Stream.class.getTypeName())) {
                return messageResult.stream();
            }
        }

        return result;
    }
}
