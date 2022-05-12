package com.debrains.debrainsApi.dto.user;

import com.debrains.debrainsApi.common.AuthProvider;
import com.debrains.debrainsApi.common.UserState;
import lombok.*;

import java.time.LocalDateTime;

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
    private boolean consent;

    private String icon;
    private Integer tier;
    private Long exp;
    private String memo;
    private String authProvider;
    private LocalDateTime lastLoginDate;
    private LocalDateTime regDate;
}
