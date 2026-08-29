package com.dreamflow.api.cache.service.implementation;

import com.dreamflow.api.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Primary
public class RedisCacheService implements CacheService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setValue(String key, Object value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, (String) value, time, timeUnit);
    }
}
