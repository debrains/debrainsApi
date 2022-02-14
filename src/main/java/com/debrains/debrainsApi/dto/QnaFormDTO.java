package com.debrains.debrainsApi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class QnaFormDTO {
    private Long userId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
