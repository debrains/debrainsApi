package com.debrains.debrainsApi.exception;

import com.debrains.debrainsApi.common.exception.ExceptionType;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private ExceptionType error;

    public ApiException(ExceptionType e) {
        super(e.getMessage());
        this.error = e;
    }
}