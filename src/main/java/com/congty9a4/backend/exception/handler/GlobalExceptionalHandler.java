package com.congty9a4.backend.exception.handler;

import com.congty9a4.backend.dto.resp.api.ErrorApiResponse;
import com.congty9a4.backend.exception.error.ErrorCode;
import com.congty9a4.backend.exception.error.AppException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Hidden
@Slf4j
@RestControllerAdvice
public class GlobalExceptionalHandler {

    @ExceptionHandler(value = AppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorApiResponse handlingAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.error(ex.getMessage());
        return ErrorApiResponse.builder()
                .message(errorCode.getDetailedMessage())
                .detail(ex.getMessage())
                .status(errorCode.getCode())
                .build();
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorApiResponse handlingNoResourceFound(NoResourceFoundException ex) {
        ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;
        return ErrorApiResponse.builder()
                .message("Resource Not Found")
                .detail(String.format("%s [%s -> %s]", errorCode.getDetailedMessage(),
                        ex.getHttpMethod().name().toUpperCase(), ex.getResourcePath()))
                .build();

    }

    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorApiResponse handlingBindException(BindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorCode errorCode = ErrorCode.INVALID_INPUT;

        return ErrorApiResponse.builder()
                .message("Invalid Request Data")
                .detail(errorCode.getDetailedMessage())
                .errors(errors)
                .build();
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorApiResponse handlingMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        ErrorCode errorCode = ErrorCode.FILE_TOO_LARGE;

        return ErrorApiResponse.builder()
                .message("Invalid request data")
                .detail(errorCode.getDetailedMessage())
                .build();
    }

    @ExceptionHandler(value = { java.security.GeneralSecurityException.class, java.io.IOException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ErrorApiResponse handlingGoogleSecurityException(Exception ex) {
        log.error("Decoding error Token Google: {}", ex.getMessage());

        ErrorCode errorCode = ErrorCode.INVALID_TOKEN;

        return ErrorApiResponse.builder()
                .message("Authentication Failed")
                .detail("Token Google Invalid, invalid format or expired.")
                .status(errorCode.getCode())
                .build();
    }
}
