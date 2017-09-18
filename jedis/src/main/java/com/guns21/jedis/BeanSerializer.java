package com.guns21.jedis;

public interface BeanSerializer {
    String deserializeKey(byte[] bytes);

    byte[] serialKey(String string);

    byte[] serialValue(Object t);

    Object deserializeValue(byte[] bytes);
}
