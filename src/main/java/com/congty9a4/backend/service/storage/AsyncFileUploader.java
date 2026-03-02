package com.congty9a4.backend.service.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

/**
 * A dedicated service for handling asynchronous file uploads.
 * By placing @Async in a separate class, we avoid self-invocation issues
 * and ensure that calls from other services are proxied correctly by Spring.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncFileUploader {

    private final StorageService storageService;

    /**
     * Uploads a single file asynchronously on a separate thread.
     *
     * @param file The file to upload.
     * @return A CompletableFuture containing the URL of the uploaded file.
     */
    @Async("taskExecutor")
    public CompletableFuture<String> uploadFileAsync(MultipartFile file) {
        String threadName = Thread.currentThread().getName();
        long startTime = System.currentTimeMillis();
        String fileName = file.getOriginalFilename();

        log.info("[{}] Starting async upload for: {}", threadName, fileName);

        try {
            String url = storageService.uploadFile(file);
            long duration = System.currentTimeMillis() - startTime;
            log.info("[{}] Upload completed in {}ms: {} -> {}", threadName, duration, fileName, url);
            return CompletableFuture.completedFuture(url);
        } catch (Exception e) {
            log.error("[{}] Failed to upload file {}: {}", threadName, fileName, e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }
}

