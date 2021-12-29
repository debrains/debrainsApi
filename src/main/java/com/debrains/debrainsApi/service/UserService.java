package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.EmailAuthDTO;
import com.debrains.debrainsApi.dto.UserDTO;
import com.debrains.debrainsApi.entity.EmailAuth;
import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.entity.UserRole;
import com.debrains.debrainsApi.repository.EmailAuthRepository;
import com.debrains.debrainsApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final EmailAuthRepository emailAuthRepository;

    private final EmailService emailService;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("SecurityUserDetailsService loadUserByUsername: " + username);

        return userRepository.findByUsername(username, false)
                .orElseThrow(() -> new UsernameNotFoundException("Check Email or Social"));
    }

    @Transactional
    public String register(UserDTO userDTO) {

        validateDuplicateUser(userDTO.getEmail());

        validateDuplicateNickname(userDTO.getNickname());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));

        log.info(userDTO.getEmail());
        log.info(UUID.randomUUID().toString());

        User user = userRepository.save(dtoToEntity(userDTO));

        issuedEmail(userDTO.getEmail());

        return user.getEmail();
    }

    /**
     * 이메일 토큰 발급
     * */
    public EmailAuth issuedEmail(String email) {

        validateEmailToken(email);

        EmailAuth emailAuth = emailAuthRepository.save(
                EmailAuth.builder()
                        .email(email)
                        .authToken(UUID.randomUUID().toString())
                        .expired(false)
                        .build());

        emailService.send(emailAuth.getEmail(), emailAuth.getAuthToken());

        return emailAuth;
    }

    private void validateDuplicateUser(String email) {

        userRepository.findByEmail(email)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 이메일입니다.");
                });
    }

    private void validateDuplicateNickname(String nickname) {

        userRepository.findByNickname(nickname)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 별명입니다.");
                });
    }

    private void validateEmailToken(String email) {

        userRepository.findByEmailAuth(email)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 이메일 인증을 완료하였습니다.");
                });
    }

    @Transactional
    public void confirmEmail(EmailAuthDTO emailAuthDTO) {

        validateEmailToken(emailAuthDTO.getEmail());

        EmailAuth emailAuth = emailAuthRepository
                .findValidAuthByEmail(emailAuthDTO.getEmail(), emailAuthDTO.getAuthToken(), LocalDateTime.now())
                .orElseThrow(() -> new UsernameNotFoundException("토큰이 유효하지 않습니다."));

        User user = userRepository.findByEmail(emailAuthDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("이메일이 없습니다."));

        emailAuth.useToken();
        user.emailVerifiedSuccess();
    }

    User dtoToEntity(UserDTO dto) {
        User entity = User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .fromSocial(dto.isFromSocial())
                .role(UserRole.valueOf(dto.getRole()))
                .emailAuth(false)
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
