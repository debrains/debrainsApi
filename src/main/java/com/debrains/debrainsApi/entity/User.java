package com.debrains.debrainsApi.entity;

import com.debrains.debrainsApi.common.AuthProvider;
import com.debrains.debrainsApi.common.UserRole;
import com.debrains.debrainsApi.common.UserState;
import com.debrains.debrainsApi.dto.UserInfoDTO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_table")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String email;

    @Column(unique = true)
    private String name;

    @Column(length = 2000)
    private String description;

    private String img;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserState state;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    private String githubUrl;
    private String blogUrl;
    private String snsUrl;

    private String icon;
    private Integer tier;
    private Long exp;
    private String memo;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginDate;

    private String refreshToken;



    /*
    * 개인정보 수정
    */
    public void updateUserInfo(UserInfoDTO dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.img = dto.getImg();
        this.githubUrl = dto.getGithubUrl();
        this.blogUrl = dto.getBlogUrl();
        this.snsUrl = dto.getSnsUrl();
    }
}
