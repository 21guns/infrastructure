package com.guns21.jackjson.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by jliu on 16/8/10.
 */
public class MoneySerializer extends JsonSerializer<Double> {
  private DecimalFormat df = new DecimalFormat("0.00");

  @Override
  public void serialize(Double aDouble, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
    jsonGenerator.writeString(df.format(aDouble));
  }
}
