package com.ktjr.tenants.boot.config;

import com.ktjr.tenants.lookup.HikariMultiTenantDataSourceLookup;
import com.ktjr.tenants.lookup.MultiTenantDataSourceLookup;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by xinjiawei on 2017/5/22.
 */
@Configuration
public class JpaConfig {

    @Autowired
    private MultiTenantConnectionProvider multiTenantConnectionProvider;
    @Autowired
    private CurrentTenantIdentifierResolver currentTenantIdentifierResolver;


    @Bean
    public MultiTenantDataSourceLookup dataSourceLookup(DataSource dataSource) {
        HikariMultiTenantDataSourceLookup dataSourceLookup = new HikariMultiTenantDataSourceLookup(dataSource);

        return dataSourceLookup;
    }
    //默认使用org.apache.tomcat.jdbc.pool.DataSource,不会根据配置使用用户指定的datasource
//    @Bean
//    @ConfigurationProperties("spring.datasource")
//    public DataSource dataSource(){
//        return DataSourceBuilder.create().build();
//    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setPackagesToScan("com.ktjr.**.**.api.entity");
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        entityManagerFactory.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.globally_quoted_identifiers", true);
        jpaProperties.put("hibernate.dialect", org.hibernate.dialect.MySQL5Dialect.class);
        jpaProperties.put("hibernate.multi_tenant_connection_provider", multiTenantConnectionProvider);
        jpaProperties.put("hibernate.tenant_identifier_resolver", currentTenantIdentifierResolver);
        jpaProperties.put("hibernate.multiTenancy", "SCHEMA");
        entityManagerFactory.setJpaProperties(jpaProperties);

        return entityManagerFactory;
    }


}
