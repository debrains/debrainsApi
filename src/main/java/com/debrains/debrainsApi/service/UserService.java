package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.UserInfoDTO;

public interface UserService {

    UserInfoDTO getUserInfo(Long id);

    void updateUserInfo(UserInfoDTO dto);
}
