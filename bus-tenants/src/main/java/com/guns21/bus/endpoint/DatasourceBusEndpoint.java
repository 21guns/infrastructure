package com.guns21.bus.endpoint;

import com.guns21.bus.event.AddDataSourceApplicationEvent;
import org.springframework.cloud.bus.endpoint.AbstractBusEndpoint;
import org.springframework.cloud.bus.endpoint.BusEndpoint;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@ManagedResource
public class DatasourceBusEndpoint extends AbstractBusEndpoint {
    public DatasourceBusEndpoint(ApplicationEventPublisher context, String id, BusEndpoint delegate) {
        super(context, id, delegate);
    }

    @RequestMapping(
        value = {"datasource"},
        method = {RequestMethod.POST}
    )
    @ResponseBody
    @ManagedOperation
    public void refresh(@RequestParam String dataSource, @RequestParam(value = "destination",required = false) String destination) {
        this.publish(new AddDataSourceApplicationEvent(this, this.getInstanceId(), destination, dataSource));
    }
}