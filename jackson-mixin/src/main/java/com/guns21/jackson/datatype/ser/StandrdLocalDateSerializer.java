package com.guns21.jackson.datatype.ser;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.format.DateTimeFormatter;

public class StandrdLocalDateSerializer extends LocalDateSerializer {
    public StandrdLocalDateSerializer(DateTimeFormatter formatter) {
        super(formatter);
    }
}
