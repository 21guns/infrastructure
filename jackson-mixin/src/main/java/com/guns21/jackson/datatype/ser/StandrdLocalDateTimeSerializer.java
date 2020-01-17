package com.guns21.jackson.datatype.ser;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.format.DateTimeFormatter;

public class StandrdLocalDateTimeSerializer extends LocalDateTimeSerializer {
    public StandrdLocalDateTimeSerializer(DateTimeFormatter formatter) {
        super(formatter);
    }
}
