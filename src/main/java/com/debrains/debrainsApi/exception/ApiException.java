package com.debrains.debrainsApi.exception;

import com.debrains.debrainsApi.common.ResponseCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private ResponseCode response;

    public ApiException(ResponseCode e) {
        super(e.getMessage());
        this.response = e;
    }
}