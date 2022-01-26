package com.debrains.debrainsApi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserInfoDTO {
    private Long id;
    private String email;
    @NotBlank
    private String name;
    private String description;
    private String img;
    private String githubUrl;
    private String blogUrl;
    private String snsUrl;
}
