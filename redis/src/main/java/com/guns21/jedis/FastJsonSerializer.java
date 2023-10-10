package com.guns21.jedis;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

public class FastJsonSerializer implements BeanSerializer {
    private final Charset charset;

    @Override
    public byte[] serialValue(Object source) {
        if (source == null) {
            return new byte[0];
        }
        return JSON.toJSONBytes(source, JSONWriter.Feature.WriteClassName);
    }

    @Override
    public Object deserializeValue(byte[] bytes) {
        if (isEmpty(bytes)) {
            return null;
        }
        return JSON.parse(new String(bytes, charset));
    }

    public FastJsonSerializer() {
        this(Charset.forName("UTF8"));
    }

    public FastJsonSerializer(Charset charset) {
        Assert.notNull(charset, "charset is null");
        this.charset = charset;
    }

    public String deserializeKey(byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }

    public byte[] serialKey(String string) {
        return (string == null ? null : string.getBytes(charset));
    }

    static boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }
}
