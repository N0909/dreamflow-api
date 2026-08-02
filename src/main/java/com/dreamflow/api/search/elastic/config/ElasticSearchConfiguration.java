package com.dreamflow.api.search.elastic.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(
        basePackages = "com.dreamflow.api.search.elastic.repository"
)
public class ElasticSearchConfiguration {
}