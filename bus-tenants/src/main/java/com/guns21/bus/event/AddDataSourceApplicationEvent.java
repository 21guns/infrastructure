package com.guns21.bus.event;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * Created by jliu on 2017/6/1.
 */
public class AddDataSourceApplicationEvent extends RemoteApplicationEvent {
    private String dataSource;

    public AddDataSourceApplicationEvent() {
    }

    public AddDataSourceApplicationEvent(Object source, String originService, String destinationService, String dataSource) {
        super(source, originService, destinationService);
        this.dataSource = dataSource;
    }

    public String getDataSource() {
        return dataSource;
    }
}
