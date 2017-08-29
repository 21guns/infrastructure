package com.ktjr.tenants.boot.config;

import com.ktjr.tenants.lookup.MultiTenantDataSourceLookup;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Created by xinjiawei on 2017/5/12.
 */
@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        return resolveTenantByHead();
    }

    public String resolveTenantByHead() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attr != null) {
            String tenantId = attr.getRequest().getHeader("X-TenantID");
            if (tenantId != null) {
                return tenantId;
            }
        }
        return MultiTenantDataSourceLookup.DEFAULT_TENANTED;

    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
