package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.common.AwsS3Uploader;
import com.debrains.debrainsApi.dto.EventDTO;
import com.debrains.debrainsApi.dto.user.ProfileDTO;
import com.debrains.debrainsApi.dto.user.UserBoardDTO;
import com.debrains.debrainsApi.dto.user.UserDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final AwsS3Uploader awsS3Uploader;
    private final ModelMapper modelMapper;

    private static String dirName = "user";

    @Override
    public UserInfoDTO getUserInfo(Long id) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        UserInfoDTO dto = modelMapper.map(entity, UserInfoDTO.class);
        return dto;
    }

    @Override
    @Transactional
    public void updateUserInfo(MultipartFile img, UserInfoDTO dto) throws IOException {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if (img != null) {
            if (user.getImg() != null) {        // 이전파일 제거
                String previous = user.getImg().substring(user.getImg().indexOf(dirName));
                awsS3Uploader.delete(previous);
            }
            String path = awsS3Uploader.upload(img, dirName);
            dto.setImg(path);
        }
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
        Profile profile = profileRepository.findByUserId(dto.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        profile.updateProfile(dto);
    }

    @Override
    public UserBoardDTO getUserBoard(Long id) {
        Profile userBoard = profileRepository.findByUserId(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        UserBoardDTO dto = toDtoBoard(userBoard);
        return dto;
    }

    @Override
    public Page<UserDTO> getUserList(Pageable pageable) {
        Page<UserDTO> userList = userRepository.findAll(pageable)
                .map(entity -> modelMapper.map(entity, UserDTO.class));
        return userList;
    }

    @Override
    public UserDTO getAdminUserInfo(Long id) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        UserDTO dto = modelMapper.map(entity, UserDTO.class);
        return dto;
    }

    @Override
    @Transactional
    public void updateAdminUserInfo(UserDTO dto) {
        User user = userRepository.getById(dto.getId());
        user.updateAdminUserInfo(dto);
    }
}
