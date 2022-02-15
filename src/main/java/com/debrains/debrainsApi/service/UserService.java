package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.user.ProfileDTO;
import com.debrains.debrainsApi.dto.user.UserBoardDTO;
import com.debrains.debrainsApi.dto.user.UserInfoDTO;
import com.debrains.debrainsApi.entity.Profile;

import java.util.Arrays;
import java.util.List;

public interface UserService {

    UserInfoDTO getUserInfo(Long id);

    void updateUserInfo(UserInfoDTO dto);

    ProfileDTO getProfile(Long userId);

    void updateProfile(ProfileDTO dto);

    UserBoardDTO getUserBoard(Long id);


    default UserBoardDTO toDtoBoard(Profile entity) {
        return UserBoardDTO.builder()
                .id(entity.getId())
                .email(entity.getUser().getEmail())
                .name(entity.getUser().getName())
                .description(entity.getUser().getDescription())
                .img(entity.getUser().getImg())
                .githubUrl(entity.getUser().getGithubUrl())
                .blogUrl(entity.getUser().getBlogUrl())
                .snsUrl(entity.getUser().getSnsUrl())
                .purpose(entity.getPurpose())
                .skills(entity.getSkills())
                .build();
    }

    default ProfileDTO toDtoProfile(Profile entity) {
        return ProfileDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .purpose(entity.getPurpose())
                .skills(Arrays.asList(entity.getSkills().split(",")))
                .build();
    }
}
