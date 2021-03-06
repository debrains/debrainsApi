package com.debrains.debrainsApi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SkillReqDTO {

    private Long id;

    @NotBlank
    private String request;
}
