package com.guns21.bus.event;

import com.ktjr.tenants.lookup.MultiTenantDataSourceLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Created by jliu on 2017/6/1.
 */
@Component
public class AddDataSourceListener {
    private static final Logger logger = LoggerFactory.getLogger(AddDataSourceListener.class);
    @Autowired
    private MultiTenantDataSourceLookup dataSourceLookup;

    @EventListener(AddDataSourceApplicationEvent.class)
    public void onApplicationEvent(AddDataSourceApplicationEvent event) {
        try {
            dataSourceLookup.getDataSource(event.getDataSource());
        } catch (Exception e) {
            logger.error("创建schema异常",e);
        }
    }
}
