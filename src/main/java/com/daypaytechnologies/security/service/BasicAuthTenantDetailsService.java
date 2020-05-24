package com.daypaytechnologies.security.service;


import com.daypaytechnologies.core.domain.FineractPlatformTenant;

public interface BasicAuthTenantDetailsService {

    FineractPlatformTenant loadTenantById(String tenantId, boolean isReport);
}