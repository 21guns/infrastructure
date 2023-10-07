package com.guns21.jackson.http.converter.json;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guns21.jackson.JacksonMixinCache;
import com.guns21.jackson.annotation.JsonResponse;
import com.guns21.jackson.wrapper.ResponseWrapper;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Adds support for {@link JsonResponse} annotation
 * 
 * @author Jack Matthews
 * 
 */
public final class JsonResponseAwareJsonMessageConverter extends MappingJackson2HttpMessageConverter {
    public JsonResponseAwareJsonMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        if (object instanceof ResponseWrapper) {
            writeJson((ResponseWrapper) object, outputMessage);
        } else {
            super.writeInternal(object, outputMessage);
        }
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (object instanceof ResponseWrapper) {
            writeJson((ResponseWrapper) object, outputMessage);
        } else {
            super.writeInternal(object, type, outputMessage);
        }

    }

    protected void writeJson(ResponseWrapper response, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {

        JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());

        ObjectMapper mapper = JacksonMixinCache.get(response.getMethodName());

        if (Objects.isNull(mapper)) {
            throw new HttpMessageNotWritableException("ObjectMapper is null for method name = " + response.getMethodName());
        }

        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
        try {
            mapper.writeValue(jsonGenerator, response.getOriginalResponse());
        } catch (IOException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

}
