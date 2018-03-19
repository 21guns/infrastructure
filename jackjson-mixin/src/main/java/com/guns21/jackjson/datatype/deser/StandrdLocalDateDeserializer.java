package com.guns21.jackjson.datatype.deser;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.format.DateTimeFormatter;

public class StandrdLocalDateDeserializer extends LocalDateDeserializer {

    public StandrdLocalDateDeserializer(DateTimeFormatter formatter) {
        super(formatter);
    }
}
