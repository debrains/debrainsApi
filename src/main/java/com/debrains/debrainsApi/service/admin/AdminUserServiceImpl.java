package com.debrains.debrainsApi.service.admin;

import com.debrains.debrainsApi.common.UserState;
import com.debrains.debrainsApi.dto.user.UserDTO;
import com.debrains.debrainsApi.dto.user.UserInfoDTO;
import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.UserRepository;
import com.debrains.debrainsApi.repository.admin.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdminUserServiceImpl implements AdminUserService{

    private final AdminUserRepository adminUserRepository;
    private final ModelMapper modelMapper;


    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<UserDTO> userList = adminUserRepository.findAll(pageable)
                .map(user -> modelMapper.map(user, UserDTO.class));
        return userList;
    }

    @Override
    public UserDTO findById(Long id) {
        User userInfo = adminUserRepository.findById(id)
                .orElseThrow();

        UserDTO user = toDtoUser(userInfo);
        return user;
    }

    @Override
    @Transactional
    public void updateUserInfo(UserDTO dto) {
        User user = adminUserRepository.getById(dto.getId());
        user.updateAdminUserInfo(dto);
    }
}
