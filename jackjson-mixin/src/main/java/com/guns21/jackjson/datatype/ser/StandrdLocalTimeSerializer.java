package com.guns21.jackjson.datatype.ser;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.format.DateTimeFormatter;

public class StandrdLocalTimeSerializer extends LocalTimeSerializer {

    public StandrdLocalTimeSerializer(DateTimeFormatter formatter) {
        super(formatter);
    }
}
