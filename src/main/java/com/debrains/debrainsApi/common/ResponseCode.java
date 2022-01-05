package com.debrains.debrainsApi.common;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ResponseCode {

    SUCCESS(HttpStatus.OK, "0000", "성공"),
    INVALID_EMAIL(HttpStatus.IM_USED, "AU01", "이미 사용중인 이메일입니다");

    private final HttpStatus status;
    private final String code;
    private String message;

    ResponseCode(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    ResponseCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
