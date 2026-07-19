package com.dreamflow.api.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Value("${executor.media-processing.core-threads}")
    private int coreThreads;
    @Value("${executor.media-processing.max-threads}")
    private int maxThreads;
    @Value("${executor.media-processing.queue-capacity}")
    private int queueCapacity;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10);
    }

    @Bean(name="mediaProcessingExecutor")
    public Executor mediaProcessingExecutor(
            @Value("${executor.media-processing.core-threads}")
            int coreThreads,
            @Value("${executor.media-processing.max-threads}")
            int maxThreads,
            @Value("${executor.media-processing.queue-capacity}")
            int queueCapacity
    ){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreThreads);
        executor.setMaxPoolSize(maxThreads);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("media-processing-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean(name = "emailExecutor")
    public Executor emailExecutor(
            @Value("${executor.email.core-threads}") int coreThreads,
            @Value("${executor.email.max-threads}") int maxThreads,
            @Value("${executor.email.queue-capacity}") int queueCapacity
    ) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreThreads);
        executor.setMaxPoolSize(maxThreads);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("email-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean(name = "homeExecutor")
    public Executor homeExecutor(
            @Value("${executor.home.core-threads}")
            int coreThreads,

            @Value("${executor.home.max-threads}")
            int maxThreads,

            @Value("${executor.home.queue-capacity}")
            int queueCapacity
    ) {

        ThreadPoolTaskExecutor executor =
                new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(coreThreads);
        executor.setMaxPoolSize(maxThreads);
        executor.setQueueCapacity(queueCapacity);

        executor.setThreadNamePrefix("home-");

        executor.setRejectedExecutionHandler(
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        executor.initialize();

        return executor;
    }

    @Bean(name="indexExecutor")
    public Executor indexExecutor(
            @Value("${executor.media-processing.core-threads}")
            int coreThreads,
            @Value("${executor.media-processing.max-threads}")
            int maxThreads,
            @Value("${executor.media-processing.queue-capacity}")
            int queueCapacity
    ){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(coreThreads);
        taskExecutor.setMaxPoolSize(maxThreads);
        taskExecutor.setQueueCapacity(queueCapacity);

        taskExecutor.setThreadNamePrefix("indexer-");

        taskExecutor.setRejectedExecutionHandler(
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        taskExecutor.initialize();

        return taskExecutor;
    }

}
