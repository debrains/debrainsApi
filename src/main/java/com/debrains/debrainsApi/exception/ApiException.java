package com.debrains.debrainsApi.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private ErrorCode errorCode;

    public ApiException(ErrorCode e) {
        super(e.getMessage());
        this.errorCode = e;
    }

    public ApiException(ErrorCode e, String message) {
        super(message);
        this.errorCode = e;
    }
}