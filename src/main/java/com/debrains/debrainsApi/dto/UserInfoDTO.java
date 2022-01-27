package com.debrains.debrainsApi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserInfoDTO {
    private Long id;
    private String email;
    @NotBlank(message = "닉네임은 필수입니다.")
    private String name;
    private String description;
    private String img;
    private String githubUrl;
    private String blogUrl;
    private String snsUrl;
}
