package com.debrains.debrainsApi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QnaDTO {
    private Long id;
    private Long userId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private boolean completed;
    private String answer;
}
