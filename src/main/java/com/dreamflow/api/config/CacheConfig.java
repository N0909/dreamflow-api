package com.dreamflow.api.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.*;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory){
        Map<String,RedisCacheConfiguration> configs = new HashMap<>();

        configs.put(
                "song",
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5))
        );

        return RedisCacheManager
                .builder(connectionFactory)
                .withInitialCacheConfigurations(configs)
                .build();
    }
}
