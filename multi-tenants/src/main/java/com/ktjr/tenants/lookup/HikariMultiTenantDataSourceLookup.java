package com.ktjr.tenants.lookup;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.net.*;

/**
 * 针对Hikari数据源的多租户配置
 */
@Component(value = "dataSourceLookup")
public class HikariMultiTenantDataSourceLookup extends MultiTenantDataSourceLookup {


    public HikariMultiTenantDataSourceLookup(DataSource dataSource) {
        super(dataSource);
    }

    protected DataSource createDataSource(String tenantId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //1.获得用户配置的datasource属性
        DataSource dataSource = this.getDataSource(DEFAULT_TENANTED);
        //2.复制HikariConfig属性创建新的HikariConfig
        DataSource hikariDataSourceConfig = ConstructorUtils.invokeConstructor(dataSource.getClass());
        BeanUtils.copyProperties(dataSource, hikariDataSourceConfig, "logWriter");
        //3.设置新的DataSource的属性
        BeanMap beanMap = BeanMap.create(hikariDataSourceConfig);
        //3.1获得url数据
        Object url = beanMap.get("jdbcUrl");
        Object poolName = beanMap.get("poolName");
        //3.2分析url数据
        String jdbcUri = url.toString().substring(5);
        URI uri = URI.create(jdbcUri);

        //3.3替换数据库schema
        jdbcUri = url.toString().replaceAll(uri.getPath(), "/" + tenantId);
        //3.4设置新数据源的url
        beanMap.put("jdbcUrl", jdbcUri);
        beanMap.put("poolName", tenantId + "_" + poolName);
        //根据配置生成新的datasource
        DataSource newDataSource = ConstructorUtils.invokeConstructor(dataSource.getClass(), hikariDataSourceConfig);


        return newDataSource;
    }


}