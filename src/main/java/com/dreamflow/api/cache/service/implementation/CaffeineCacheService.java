package com.dreamflow.api.cache.service.implementation;
import com.dreamflow.api.cache.service.CacheService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CaffeineCacheService implements CacheService {
    @Value("${cache.max-size}")
    private int maxSize;

    Cache<String, Object> cache = Caffeine
            .newBuilder()
            .maximumSize(maxSize)
            .build();

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public void setValue(String key, Object value, long time, TimeUnit timeUnit) {
        cache.put(key, value);
    }
}
