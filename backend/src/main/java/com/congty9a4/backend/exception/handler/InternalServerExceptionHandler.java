package com.congty9a4.backend.exception.handler;


import com.congty9a4.backend.dto.resp.api.ErrorApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Hidden
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ErrorApiResponse handleRuntimeException(RuntimeException ex) {
        return ErrorApiResponse.builder()
                .message("Internal Server Error")
                .build();
    }

}
