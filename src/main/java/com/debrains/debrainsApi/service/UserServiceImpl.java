package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.UserInfoDTO;
import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserInfoDTO getUserInfo(Long id) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Not Exist User"));
        UserInfoDTO dto = modelMapper.map(entity, UserInfoDTO.class);
        return dto;
    }

    @Override
    @Transactional
    public void updateUserInfo(UserInfoDTO dto) {
        User user = userRepository.getById(dto.getId());
        user.updateUserInfo(dto);
    }
}
