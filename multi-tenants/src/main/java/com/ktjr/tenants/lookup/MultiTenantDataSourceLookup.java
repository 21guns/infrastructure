package com.ktjr.tenants.lookup;

import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jliu on 2017/5/31.
 *
 * @see org.springframework.jdbc.datasource.lookup.MapDataSourceLookup
 */
public abstract class MultiTenantDataSourceLookup implements DataSourceLookup {
    public static final String DEFAULT_TENANTED = "default_tenant";

    private final Map<String, DataSource> dataSources = new ConcurrentHashMap<>(16);

    private boolean createable = true;

    public MultiTenantDataSourceLookup(DataSource dataSource) {
        this.addDataSource(DEFAULT_TENANTED, dataSource);
    }

    protected abstract DataSource createDataSource(String tenantId) throws Exception;

    public void addDataSource(String dataSourceName, DataSource dataSource) {
        Assert.notNull(dataSourceName, "DataSource name must not be null");
        Assert.notNull(dataSource, "DataSource must not be null");
        this.dataSources.put(dataSourceName, dataSource);
    }

    @Override
    public DataSource getDataSource(String dataSourceName) throws DataSourceLookupFailureException {
        Assert.notNull(dataSourceName, "DataSource name must not be null");
        DataSource dataSource = (DataSource) this.dataSources.get(dataSourceName);

        if (dataSource == null) {
            if (isCreateable()) {
                try {
                    dataSource = createDataSource(dataSourceName);
                    this.addDataSource(dataSourceName, dataSource);
                } catch (Exception e) {
                    throw new DataSourceLookupFailureException("create datasource :", e);
                }
            } else {
                throw new DataSourceLookupFailureException("No DataSource with name '" + dataSourceName + "' registered");
            }
        }
        return dataSource;
    }

    public boolean isCreateable() {
        return createable;
    }

    public MultiTenantDataSourceLookup setCreateable(boolean createable) {
        this.createable = createable;
        return this;
    }
}
