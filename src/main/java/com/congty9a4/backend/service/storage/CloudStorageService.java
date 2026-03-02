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

    //@Async makes this function is executed in seperate thread
    //this works as wrapper for the uploadFile,
    //allowing it to be called asynchronously in bulkUpload and singleUpload
    @Async("taskExecutor")
    public CompletableFuture<String> uploadFileAsync(MultipartFile file) {
        log.debug("Uploading file asynchronously: {} using configured storage provider", file.getOriginalFilename());
        String url = storageService.uploadFile(file);
        return CompletableFuture.completedFuture(url);
    }

    public String uploadFile(MultipartFile file) {
        return uploadFileAsync(file)
            .thenApply(url -> {

                log.debug("File uploaded successfully: {} with URL: {}", file.getOriginalFilename(), url);
                return url;
            })
            .join();

    }

    public List<String> bulkUpload(List<MultipartFile> files) {
        log.debug("Starting bulk upload for {} files", files.size());

        List<CompletableFuture<String>> futures = files.stream()
                .map(this::uploadFileAsync)  // ← File1→Thread1, File2→Thread2, File3→Thread3
                .toList();

        // Combine all futures into one "master" future
        // This doesn't block - just creates a composite future
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        // Wait for completion and collect URLs
        allFutures.join();

        List<String> urls = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        log.debug("Bulk upload completed. {} files uploaded successfully", urls.size());
        return urls;
    }

    public String deleteFile(String fileId) {
        log.debug("Deleting file: {} using configured storage provider", fileId);
        return storageService.deleteFile(fileId);
    }
}

