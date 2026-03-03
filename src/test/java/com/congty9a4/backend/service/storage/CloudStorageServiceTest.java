package com.congty9a4.backend.service.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("CloudStorageService Tests")
class CloudStorageServiceTest {

    @Mock
    private StorageService storageService;

    @Mock
    private AsyncFileUploader asyncFileUploader;

    @InjectMocks
    private CloudStorageService cloudStorageService;

    private MockMultipartFile file;

    @BeforeEach
    void setUp() {
        file = new MockMultipartFile(
                "file",
                "hello.txt",
                "text/plain",
                "Hello, World!".getBytes()
        );
    }

    @Test
    @DisplayName("uploadFile_should_returnUrl_when_fileIsValid")
    void uploadFile_should_returnUrl_when_fileIsValid() {
        // Arrange
        String expectedUrl = "http://example.com/hello.txt";
        when(storageService.uploadFile(any(MultipartFile.class))).thenReturn(expectedUrl);

        // Act
        String actualUrl = cloudStorageService.uploadFile(file);

        // Assert
        assertEquals(expectedUrl, actualUrl);
        verify(storageService, times(1)).uploadFile(file);
    }

    @Test
    @DisplayName("bulkUpload_should_returnListOfUrls_when_filesAreValid")
    void bulkUpload_should_returnListOfUrls_when_filesAreValid() {
        // Arrange
        List<MultipartFile> files = List.of(file, file);
        String url1 = "http://example.com/file1.txt";
        String url2 = "http://example.com/file2.txt";
        when(asyncFileUploader.uploadFileAsync(any(MultipartFile.class)))
                .thenReturn(CompletableFuture.completedFuture(url1))
                .thenReturn(CompletableFuture.completedFuture(url2));

        // Act
        List<String> actualUrls = cloudStorageService.bulkUpload(files);

        // Assert
        assertNotNull(actualUrls);
        assertEquals(2, actualUrls.size());
        assertEquals(List.of(url1, url2), actualUrls);
        verify(asyncFileUploader, times(2)).uploadFileAsync(any(MultipartFile.class));
    }

    @Test
    @DisplayName("bulkUpload_should_returnEmptyList_when_noFilesProvided")
    void bulkUpload_should_returnEmptyList_when_noFilesProvided() {
        // Arrange
        List<MultipartFile> emptyList = Collections.emptyList();

        // Act
        List<String> result = cloudStorageService.bulkUpload(emptyList);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(asyncFileUploader, never()).uploadFileAsync(any());
    }

    @Test
    @DisplayName("deleteFile_should_returnSuccessMessage_when_fileIdIsValid")
    void deleteFile_should_returnSuccessMessage_when_fileIdIsValid() {
        // Arrange
        String fileId = "some-file-id";
        String expectedResponse = "File deleted successfully";
        when(storageService.deleteFile(fileId)).thenReturn(expectedResponse);

        // Act
        String response = cloudStorageService.deleteFile(fileId);

        // Assert
        assertEquals(expectedResponse, response);
        verify(storageService, times(1)).deleteFile(fileId);
    }
}

