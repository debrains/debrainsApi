package com.debrains.debrainsApi.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;
    private String email;
    private String name;
    private String description;
    private String img;
    private String role;
    private String state;
    private String githubUrl;
    private String blogUrl;
    private String snsUrl;

    private String icon;
    private Integer tier;
    private Long exp;
}
