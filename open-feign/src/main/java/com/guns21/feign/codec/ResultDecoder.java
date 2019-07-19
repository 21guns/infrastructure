package com.guns21.feign.codec;

import com.guns21.data.domain.result.MessageResult;
import feign.Response;
import feign.jackson.JacksonDecoder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by Liu Xiang on 2019-07-19.
 */
public class ResultDecoder extends JacksonDecoder {
    @Override
    public Object decode(Response response, Type type) throws IOException {
        Type shadeType = type;
        if (Arrays.asList(Optional.class, Stream.class).contains(type.getClass())) {
            shadeType = MessageResult.class;
        }

        Object result = super.decode(response, shadeType);
        if (result instanceof MessageResult) {
            MessageResult messageResult = (MessageResult) result;
            if (Objects.equals(Optional.class, type)) {
                return messageResult.optional();
            }

            if (Objects.equals(Stream.class, type)) {
                return messageResult.stream();
            }
        }

        return result;
    }
}
