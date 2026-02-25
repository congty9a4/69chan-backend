package com.congty9a4.backend.service.storage;

import com.congty9a4.backend.config.cloud.BucketConfig;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

/**
 * Google Cloud Storage implementation for file uploads
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.provider", havingValue = "gcs", matchIfMissing = true)
public class GcsStorageService implements StorageService {

    private final Storage storage;
    private final BucketConfig bucketConfig;

    @Override
    public String uploadFile(MultipartFile file, String _fileName) {
        String fileName = String.join("/", bucketConfig.getSubDirectory(), _fileName);
        BlobId blobId = BlobId.of(bucketConfig.getBucketName(), fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .setAcl(Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
                .build();
        try {
            storage.create(blobInfo, file.getBytes());
        } catch (IOException e) {
            log.error("Failed to upload file to GCS: {}", _fileName, e);
            throw new RuntimeException("Failed to upload file to GCS", e);
        }

        String publicUrl = "https://storage.googleapis.com/" + bucketConfig.getBucketName() + "/" + fileName;

        log.info("New file uploaded to GCS with link: {}", publicUrl);
        return publicUrl;
    }

    @Override
    public String deleteFiles(String fileId) {
        return "";
    }
}

