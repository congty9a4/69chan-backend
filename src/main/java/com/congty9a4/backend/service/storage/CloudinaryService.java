package com.congty9a4.backend.service.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.congty9a4.backend.exception.error.AppException;
import com.congty9a4.backend.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Cloudinary implementation for file uploads
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.provider", havingValue = "cloudinary")
public class CloudinaryService implements StorageService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            // Remove file extension for cloudinary public_id
            String fileName = String.join("-", UUID.randomUUID().toString(), file.getOriginalFilename());
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
            log.error("Failed to upload file to Cloudinary: {}", file.getOriginalFilename(), e);
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED, "Failed to upload file to Cloudinary");
        }
    }

    @Override
    public String deleteFile(String fileId) {
        try {
        Map<String, Object> deleteResult = cloudinary.uploader().destroy(fileId, ObjectUtils.emptyMap());
            log.info("File deleted from Cloudinary: {}, result: {}", fileId, deleteResult);
            return (String) deleteResult.get("result");
        } catch (IOException e) {
            log.error("Failed to delete file from Cloudinary: {}", fileId, e);
            throw new AppException(ErrorCode.FILE_DELETE_FAILED, "Failed to delete file from Cloudinary");
        }
    }
}

