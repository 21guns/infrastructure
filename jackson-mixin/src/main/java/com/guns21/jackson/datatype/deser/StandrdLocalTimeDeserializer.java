package com.guns21.jackson.datatype.deser;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;

import java.time.format.DateTimeFormatter;

public class StandrdLocalTimeDeserializer extends LocalTimeDeserializer {
    public StandrdLocalTimeDeserializer(DateTimeFormatter formatter) {
        super(formatter);
    }
}
