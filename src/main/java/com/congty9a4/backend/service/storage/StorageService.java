package com.congty9a4.backend.service.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * Common interface for media storage services
 */
public interface StorageService {
    /**
     * Upload a file to the configured storage provider
     *
     * @param file     the file to upload
     * @param fileName the name to use for the file
     * @return the public URL of the uploaded file
     */
    String uploadFile(MultipartFile file, String fileName);
}

