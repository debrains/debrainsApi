package com.debrains.debrainsApi.service.admin;

import com.debrains.debrainsApi.common.UserState;
import com.debrains.debrainsApi.dto.user.UserBoardDTO;
import com.debrains.debrainsApi.dto.user.UserDTO;
import com.debrains.debrainsApi.entity.Profile;
import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.repository.UserRepository;
import com.debrains.debrainsApi.repository.admin.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AdminUserService{

    Page<UserDTO> findAll(Pageable pageable);

    UserDTO findById(Long id);

    void updateUserInfo(UserDTO dto);



    default UserDTO toDtoUser(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .description(user.getDescription())
                .img(user.getImg())
                .githubUrl(user.getGithubUrl())
                .blogUrl(user.getBlogUrl())
                .snsUrl(user.getSnsUrl())
                .exp(user.getExp())
                .tier(user.getTier())
                .memo(user.getMemo())
                .role(user.getRole().getRole())
                .state(user.getState().getState())
                .lastLoginDate(user.getLastLoginDate())
                .regDate(user.getRegDate())
                .authProvider(user.getAuthProvider().name())
                .build();
    }
}
