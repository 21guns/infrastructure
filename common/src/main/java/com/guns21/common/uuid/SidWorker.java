package com.guns21.common.uuid;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @see https://github.com/adyliu/idcenter/blob/master/src/main/java/com/sohu/idcenter/SidWorker.java
 */
public class SidWorker {

    private static long lastTimestamp = -1L;
    private static int sequence = 0;
    private static final long MAX_SEQUENCE = 100;
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    /**
     * 19 bits number with timestamp (20160628175532000002)
     * 15 bits number with timestamp (147166308469619)
     *
     * @return 19 bits number with timestamp
     */
    public static synchronized long nextSid() {
        long now = timeGen();
        if (now == lastTimestamp) {
            if (sequence++ > MAX_SEQUENCE) {
                now = tilNextMillis(lastTimestamp);
                sequence = 0;
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = now;
        //
        return MAX_SEQUENCE * Long.parseLong(format.format(new Date(now))) + sequence;
//        return MAX_SEQUENCE * now+ sequence;
    }

    private static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private static long timeGen() {
        return System.currentTimeMillis();
    }


}