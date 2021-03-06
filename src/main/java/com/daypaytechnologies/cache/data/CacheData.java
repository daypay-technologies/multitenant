package com.daypaytechnologies.cache.data;


import com.daypaytechnologies.core.data.EnumOptionData;

public class CacheData {

    @SuppressWarnings("unused")
    private final EnumOptionData cacheType;
    @SuppressWarnings("unused")
    private final boolean enabled;

    public static CacheData instance(final EnumOptionData cacheType, final boolean enabled) {
        return new CacheData(cacheType, enabled);
    }

    private CacheData(final EnumOptionData cacheType, final boolean enabled) {
        this.cacheType = cacheType;
        this.enabled = enabled;
    }
}