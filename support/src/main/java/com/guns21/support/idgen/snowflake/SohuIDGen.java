package com.guns21.support.idgen.snowflake;

import com.guns21.support.idgen.IDGen;
import com.sohu.idcenter.IdWorker;

public class SohuIDGen extends IdWorker implements IDGen {
    public SohuIDGen(long idepoch) {
        super(idepoch);
    }

    public SohuIDGen(long workerId, long datacenterId, long sequence) {
        super(workerId, datacenterId, sequence);
    }

    public SohuIDGen(long workerId, long datacenterId, long sequence, long idepoch) {
        super(workerId, datacenterId, sequence, idepoch);
    }
}
