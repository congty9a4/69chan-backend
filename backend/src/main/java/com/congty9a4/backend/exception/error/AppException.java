package com.congty9a4.backend.exception.error;

import com.congty9a4.backend.exception.ErrorCode;

public class AppException extends RuntimeException{
    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
