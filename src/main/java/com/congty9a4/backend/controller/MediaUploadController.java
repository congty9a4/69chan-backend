package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.service.storage.CloudStorageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MediaUploadController {


        CloudStorageService storageService;

        @PostMapping(consumes = "multipart/form-data")
        @Operation(summary = "Upload media", description = "Upload media file and return its URL")
        public ApiResponse<List<String>> uploadMedia(@RequestPart("files") List<MultipartFile> files) {
                return ApiResponse.success(storageService.bulkUpload(files));

        }


        @PostMapping(value = "/sync", consumes = "multipart/form-data")
        @Operation(summary = "Upload media", description = "Upload media file and return its URL")
        public ApiResponse<List<String>> uploadSyncMedia(@RequestPart("files") List<MultipartFile> files) {
                return ApiResponse.success(storageService.bulkSyncUpload(files));
        }

        @DeleteMapping
        @Operation(summary = "Delete media", description = "Delete media file by URL")
        public ApiResponse<Void> deleteMedia(@RequestParam String url) {
                storageService.deleteFile(url);
                return ApiResponse.success(null);
        }
}
