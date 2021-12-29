package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.UserDTO;
import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.entity.UserRole;
import com.debrains.debrainsApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("SecurityUserDetailsService loadUserByUsername: " + username);

        return userRepository.findByUsername(username, false)
                .orElseThrow(() -> new UsernameNotFoundException("Check Email or Social"));
    }

    public String register(UserDTO userDTO) {

        validateDuplicateUser(userDTO.getEmail());

        validateDuplicateNickname(userDTO.getNickname());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));

        return userRepository.save(dtoToEntity(userDTO)).getEmail();
    }

    private void validateDuplicateUser(String email) {

        userRepository.findByEmail(email)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    private void validateDuplicateNickname(String nickname){

        userRepository.findByNickname(nickname)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 별명입니다.");
                });
    }

    User dtoToEntity(UserDTO dto) {
        User entity = User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .fromSocial(dto.isFromSocial())
                .role(UserRole.valueOf(dto.getRole()))
                .build();
        return entity;
    }

    UserDTO entityToDTO(User entity) {
        UserDTO dto = UserDTO.builder()
                .email(entity.getEmail())
                .password(entity.getPassword())
                .nickname(entity.getNickname())
                .fromSocial(entity.isFromSocial())
                .role(entity.getRole().name())
                .build();
        return dto;
    }
}
