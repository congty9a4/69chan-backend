package com.congty9a4.backend.controller;

import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.service.storage.CloudStorageService;
import com.congty9a4.backend.service.storage.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/files")

public class MediaUploadController {

        @Autowired
        CloudStorageService storageService;

        @PostMapping(consumes = "multipart/form-data")
        @Operation(summary = "Upload media", description = "Upload media file and return its URL")
        public ApiResponse<List<String>> uploadMedia(@RequestPart("files") List<MultipartFile> files) {
                var body = ApiResponse.success(storageService.bulkUpload(files));

                return body;
        }
}
