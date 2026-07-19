package com.dreamflow.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfig {
    @Bean
    public RestClient restClient(){
        return RestClient.builder()
                .baseUrl("http://localhost:8000")
                .defaultHeader("Accept","application/json")
                .build();
    }
}
