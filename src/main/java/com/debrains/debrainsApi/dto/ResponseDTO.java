package com.debrains.debrainsApi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseDTO {

    private String resCode;
    private String resMessage;

    @Builder
    public ResponseDTO(String resCode, String resMessage) {
        this.resCode = resCode;
        this.resMessage = resMessage;
    }
}
