package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.user.ProfileDTO;
import com.debrains.debrainsApi.dto.user.UserBoardDTO;
import com.debrains.debrainsApi.dto.user.UserInfoDTO;
import com.debrains.debrainsApi.entity.Profile;
import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.exception.ErrorCode;
import com.debrains.debrainsApi.repository.ProfileRepository;
import com.debrains.debrainsApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserInfoDTO getUserInfo(Long id) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        UserInfoDTO dto = modelMapper.map(entity, UserInfoDTO.class);
        return dto;
    }

    @Override
    @Transactional
    public void updateUserInfo(UserInfoDTO dto) {
        User user = userRepository.getById(dto.getId());
        user.updateUserInfo(dto);
    }

    @Override
    public ProfileDTO getProfile(Long userId) {
        Profile entity = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        ProfileDTO dto = toDtoProfile(entity);
        return dto;
    }

    @Override
    @Transactional
    public void updateProfile(ProfileDTO dto) {
        Profile profile = profileRepository.getById(dto.getId());
        profile.updateProfile(dto);
    }

    @Override
    public UserBoardDTO getUserBoard(Long id) {
        Profile userBoard = profileRepository.findByUserId(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        UserBoardDTO dto = toDtoBoard(userBoard);
        return dto;
    }
}
