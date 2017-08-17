package com.ktjr.tenants.boot.config;

import com.ktjr.tenants.lookup.MultiTenantDataSourceLookup;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Created by xinjiawei on 2017/5/22.
 */
@Component
public class MultitenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

  private static final Logger logger = LoggerFactory.getLogger(MultitenantConnectionProviderImpl.class);

  @Autowired
  @Qualifier("dataSourceLookup")
  private MultiTenantDataSourceLookup dataSourceLookup;

  @Override
  protected DataSource selectAnyDataSource() {
    return dataSourceLookup.getDataSource(MultiTenantDataSourceLookup.DEFAULT_TENANTED);
  }

  @Override
  protected DataSource selectDataSource(String tenantIdentifier) {
    //FIXME 默认相信传过来的schema，没有就进行创建
    return dataSourceLookup.getDataSource(tenantIdentifier);
  }
}
