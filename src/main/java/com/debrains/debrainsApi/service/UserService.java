package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.SkillDTO;
import com.debrains.debrainsApi.dto.SkillReqDTO;
import com.debrains.debrainsApi.dto.user.ProfileDTO;
import com.debrains.debrainsApi.dto.user.UserBoardDTO;
import com.debrains.debrainsApi.dto.user.UserDTO;
import com.debrains.debrainsApi.dto.user.UserInfoDTO;
import com.debrains.debrainsApi.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public interface UserService {

    UserInfoDTO getUserInfo(Long id);

    void updateUserInfo(MultipartFile img, UserInfoDTO dto) throws IOException;

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
                .skills(Arrays.asList(entity.getSkills().split(",")))
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

    Page<UserDTO> getUserList(Pageable pageable);

    UserDTO getAdminUserInfo(Long id);

    void updateAdminUserInfo(UserDTO dto);

    /**
     * 관심사 (skill)
     */
    Page<SkillDTO> getSkillList(Pageable pageable);

    List<String> getCategories();

    void saveSkill(SkillDTO dto);

    Page<SkillReqDTO> getSkillreqList(Pageable pageable);

    void deleteSkill(Long id);

    void deleteSkillreq(Long id);
}
