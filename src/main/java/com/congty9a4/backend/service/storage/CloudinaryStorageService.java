package com.congty9a4.backend.service.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Cloudinary implementation for file uploads
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.provider", havingValue = "cloudinary")
public class CloudinaryStorageService implements StorageService {

    private final Cloudinary cloudinary;

    @Override
    @SuppressWarnings("unchecked")
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            // Remove file extension for cloudinary public_id
            String publicId = fileName.substring(0, fileName.lastIndexOf('.'));

            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "resource_type", "auto" // Automatically detect resource type (image/video/raw)
                    )
            );

            String publicUrl = (String) uploadResult.get("secure_url");
            log.info("New file uploaded to Cloudinary with link: {}", publicUrl);
            return publicUrl;

        } catch (IOException e) {
            log.error("Failed to upload file to Cloudinary: {}", fileName, e);
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }
    }
}

