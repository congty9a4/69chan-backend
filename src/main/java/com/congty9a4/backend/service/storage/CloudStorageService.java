package com.congty9a4.backend.service.storage;

import com.congty9a4.backend.constant.MEDIA;
import com.congty9a4.backend.entity.post.MediaInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudStorageService {

    private final AsyncFileUploader asyncFileUploader;

    public String uploadFile(MultipartFile file) {
        CompletableFuture<String> future = asyncFileUploader.uploadFileAsync(file);
        String url = future.join(); // Wait for the async upload to complete
        log.info("File uploaded successfully: {}, URL: {}", file.getOriginalFilename(), url);
        return url;
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

    public void deleteFile(String url) {
        asyncFileUploader.deleteFileAsync(toMediaInfo(url).getId()).join();
    }

    public MediaInfo toMediaInfo(String url) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        String type = MEDIA.getType(fileName);
        String publicId = fileName.substring(0, fileName.lastIndexOf('.'));
        return MediaInfo.builder()
                .url(url)
                .mediaType(type)
                .id(publicId)
                .build();
    }
}

