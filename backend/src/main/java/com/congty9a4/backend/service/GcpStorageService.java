package com.congty9a4.backend.service;

import com.congty9a4.backend.config.BucketConfig;
import com.google.cloud.storage.Acl;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.Collections;



@Slf4j
@Service
public class GcpStorageService {

    @Autowired
    private Storage storage;

    @Autowired
    private BucketConfig bucketConfig;

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = String.join("/", bucketConfig.getSubDirectory(), file.getOriginalFilename());
        BlobId blobId = BlobId.of(bucketConfig.getBucketName(), fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .setAcl(Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
                .build();
        storage.create(blobInfo, file.getBytes());

        String publicUrl = "https://storage.googleapis.com/" + bucketConfig.getBucketName() + "/" + fileName;

        log.info("New file uploaded to GCS with link: {}", publicUrl);
        return publicUrl;
    }
}

