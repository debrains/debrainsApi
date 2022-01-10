package com.debrains.debrainsApi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder
public class ResponseDTO<T> {
    private String code;
    private String message;
    private List<T> data;
}
