package com.debrains.debrainsApi.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class Message {

    private String statusCode;
    private String responseMessage;

    @Builder
    public Message(HttpStatus status, String statusCode, String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
    }
}
