package com.congty9a4.backend.exception.handler;

import com.congty9a4.backend.dto.resp.api.ApiResponse;
import com.congty9a4.backend.dto.resp.api.ErrorApiResponse;
import com.congty9a4.backend.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Hidden
@Slf4j
@RestControllerAdvice
public class GlobalExceptionalHandler {

    @ExceptionHandler(value = NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorApiResponse handlingNoResourceFound(NoResourceFoundException ex){
        ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;
        return ErrorApiResponse.builder()
                .code(errorCode.getCode())
                .message("Resource Not Found")
                .detail(errorCode.getDetailedMessage())
                .build();


    }

}
