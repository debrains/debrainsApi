package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.UserDTO;
import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.common.UserRole;
import com.debrains.debrainsApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("SecurityUserDetailsService loadUserByUsername: " + username);

        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Check Email or Social"));
    }

    User dtoToEntity(UserDTO dto) {
        User entity = User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(UserRole.valueOf(dto.getRole()))
                .build();
        return entity;
    }

    UserDTO entityToDTO(User entity) {
        UserDTO dto = UserDTO.builder()
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole().name())
                .build();
        return dto;
    }
}
