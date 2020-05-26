package com.daypaytechnologies.cache.service;

import com.daypaytechnologies.cache.domain.CacheType;

import java.util.Map;


public interface CacheWritePlatformService {

    Map<String, Object> switchToCache(CacheType cacheType);
}