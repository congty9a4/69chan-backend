package com.congty9a4.backend.service.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudStorageService {

    private final StorageService storageService;
    private final AsyncFileUploader asyncFileUploader;

    public String uploadFile(MultipartFile file) {
        log.debug("Executing synchronous upload for: {}", file.getOriginalFilename());
        return storageService.uploadFile(file);
    }

    public List<String> bulkUpload(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }

        long startTime = System.currentTimeMillis();
        log.info("Starting bulk upload for {} files.", files.size());

        // Delegate async calls to the dedicated uploader service
        List<CompletableFuture<String>> futures = files.stream()
                .map(asyncFileUploader::uploadFileAsync)
                .toList();

        // Wait for all concurrent uploads to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Collect the results
        List<String> urls = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        long duration = System.currentTimeMillis() - startTime;
        log.info("Bulk upload completed: {} files in {}ms (avg {}ms/file)",
                urls.size(), duration, duration / Math.max(urls.size(), 1));

        return urls;
    }

    public String deleteFile(String fileId) {
        log.debug("Deleting file: {}", fileId);
        return storageService.deleteFile(fileId);
    }
}

