package com.congty9a4.backend.service.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Facade service for cloud storage operations.
 * Delegates to the configured storage provider (GCS or Cloudinary).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CloudStorageService {

    private final StorageService storageService;

    public String uploadFile(MultipartFile file, String fileName) {
        log.debug("Uploading file: {} using configured storage provider", fileName);
        return storageService.uploadFile(file, fileName);
    }
}

