package com.congty9a4.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuration for Spring's async task execution.
 * Enables @Async annotation and configures a thread pool for async operations.
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Configure the task executor for @Async methods.
     * Optimized for I/O-bound operations like file uploads.
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // For I/O-bound tasks (file uploads), we can have more threads
        // since most time is spent waiting for network, not using CPU
        executor.setCorePoolSize(10);      // Increased from 5
        executor.setMaxPoolSize(50);       // Increased from 20
        executor.setQueueCapacity(200);    // Increased from 100

        // Thread name prefix for easier debugging
        executor.setThreadNamePrefix("file-upload-");

        // Rejection policy - caller runs if queue is full
        executor.setRejectedExecutionHandler(
            new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy()
        );

        // Graceful shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();

        log.info("Async task executor configured: core={}, max={}, queue={}",
                executor.getCorePoolSize(),
                executor.getMaxPoolSize(),
                executor.getQueueCapacity());

        return executor;
    }
}

