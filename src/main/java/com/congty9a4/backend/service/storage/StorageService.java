package com.congty9a4.backend.service.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * Common interface for media storage services
 */
public interface StorageService {
    String uploadFile(MultipartFile file);
    String deleteFile(String fileId);
}

