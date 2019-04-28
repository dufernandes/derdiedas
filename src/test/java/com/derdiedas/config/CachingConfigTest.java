package com.derdiedas.config;

import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.*;

class CachingConfigTest {

    @Test
    void cacheManager_noParameters_correctCacheElements() {
        CachingConfig cachingConfig = new CachingConfig();
        CacheManager cacheManager = cachingConfig.cacheManager();
        assertNotNull(cacheManager);
    }
}