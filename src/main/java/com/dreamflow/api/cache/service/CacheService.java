package com.dreamflow.api.cache.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public interface CacheService {
    Object getValue(String key);
    void setValue(String key, Object value, long time, TimeUnit timeUnit);
}
