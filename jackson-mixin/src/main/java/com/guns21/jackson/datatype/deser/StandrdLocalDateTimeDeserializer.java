package com.guns21.jackson.datatype.deser;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.format.DateTimeFormatter;

public class StandrdLocalDateTimeDeserializer extends LocalDateTimeDeserializer {
    public StandrdLocalDateTimeDeserializer(DateTimeFormatter formatter) {
        super(formatter);
    }
}
