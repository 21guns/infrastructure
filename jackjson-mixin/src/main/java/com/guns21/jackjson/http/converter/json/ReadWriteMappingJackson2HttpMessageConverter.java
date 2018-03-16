package com.guns21.jackjson.http.converter.json;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.lang.Nullable;
import org.springframework.util.TypeUtils;

import java.io.*;
import java.lang.reflect.Type;

/**
 * @author jliu
 */
public class ReadWriteMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {


    @Nullable
    private PrettyPrinter _ssePrettyPrinter;


    protected ObjectMapper writeObjectMapper;
    protected ObjectMapper readObjectMapper;

    public ReadWriteMappingJackson2HttpMessageConverter(ObjectMapper objectMapper) throws IllegalAccessException {
        super(objectMapper);
        readObjectMapper = objectMapper;
        writeObjectMapper = objectMapper.copy();


        _ssePrettyPrinter = (PrettyPrinter) FieldUtils.getField(MappingJackson2HttpMessageConverter.class, "ssePrettyPrinter",true).get(this);

    }

    /**
     * 该mapper用来写json信息
     * @return
     */
    public ObjectMapper getWriteObjectMapper() {
        return writeObjectMapper;
    }


    /**
     * @see org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter
     *
     * @param object
     * @param type
     * @param outputMessage
     * @throws IOException
     * @throws HttpMessageNotWritableException
     */
    @Override
    protected void writeInternal(Object object, @Nullable Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {



        MediaType contentType = outputMessage.getHeaders().getContentType();
        JsonEncoding encoding = getJsonEncoding(contentType);
        JsonGenerator generator = writeObjectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
        try {
            writePrefix(generator, object);

            Class<?> serializationView = null;
            FilterProvider filters = null;
            Object value = object;
            JavaType javaType = null;
            if (object instanceof MappingJacksonValue) {
                MappingJacksonValue container = (MappingJacksonValue) object;
                value = container.getValue();
                serializationView = container.getSerializationView();
                filters = container.getFilters();
            }
            if (type != null && TypeUtils.isAssignable(type, value.getClass())) {
                javaType = getJavaType(type, null);
            }
            ObjectWriter objectWriter;
            if (serializationView != null) {
                objectWriter = writeObjectMapper.writerWithView(serializationView);
            }
            else if (filters != null) {
                objectWriter = writeObjectMapper.writer(filters);
            }
            else {
                objectWriter = writeObjectMapper.writer();
            }
            if (javaType != null && javaType.isContainerType()) {
                objectWriter = objectWriter.forType(javaType);
            }
            SerializationConfig config = objectWriter.getConfig();
            if (contentType != null && contentType.isCompatibleWith(MediaType.TEXT_EVENT_STREAM) &&
                    config.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
                objectWriter = objectWriter.with(this._ssePrettyPrinter);
            }
            objectWriter.writeValue(generator, value);

            writeSuffix(generator, object);
            generator.flush();

        }
        catch (InvalidDefinitionException ex) {
            throw new HttpMessageConversionException("Type definition error: " + ex.getType(), ex);
        }
        catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getOriginalMessage(), ex);
        }
    }

}
