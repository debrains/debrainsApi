package com.debrains.debrainsApi.dto.user;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserBoardDTO {
    private Long id;
    private String email;
    private String name;
    private String description;
    private String img;
    private String githubUrl;
    private String blogUrl;
    private String snsUrl;
    private String purpose;
    private String skills;
}
