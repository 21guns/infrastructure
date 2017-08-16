package com.guns21.bus.config;

import com.guns21.bus.endpoint.DatasourceBusEndpoint;
import org.springframework.cloud.bus.endpoint.BusEndpoint;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RemoteApplicationEventScan(basePackages={"com.ktjr.bus.event"})
public class BusConfiguration {
    @Bean
    public DatasourceBusEndpoint datasourceBusEndpoint(ApplicationContext context, BusEndpoint busEndpoint) {
        return new DatasourceBusEndpoint(context, context.getId(), busEndpoint);
    }


}