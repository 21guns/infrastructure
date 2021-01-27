package com.guns21.support.idgen.snowflake;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guns21.data.domain.result.MessageResult;
import com.guns21.feign.target.SpringSessionHeaderTokenTarget;
import com.guns21.support.idgen.IDGen;
import feign.Feign;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 使用 leaf 作为id center
 * @see https://github.com/Meituan-Dianping/Leaf/blob/master/README_CN.md
 */
@Slf4j
public class LeadIDGen implements IDGen {

    private final String url;
    private final LeadIDService leadIDService;
    private final ObjectMapper objectMapper;

    public LeadIDGen(String url) {
        this.url = url;
        objectMapper = new ObjectMapper();
        leadIDService = Feign.builder()
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper))
                .target(LeadIDService.class,url);
    }

    @Override
    public long getId() {
        return leadIDService.getId("id");
    }

    @Override
    public long getWorkerId() {
        return 0;
    }


    @Headers("Content-Type: application/json;charset=UTF-8")
    public interface LeadIDService {


        @RequestLine("GET /api/snowflake/get/{key}")
        long getId(@Param("key") String key);
    }

}
