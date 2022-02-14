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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ModelMapper modelMapper;

    User user;
    UserInfoDTO userInfoDto;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .email("dev@dev.com")
                .name("dev")
                .description("I'm Developer")
                .githubUrl("http://github.com/devdev")
                .blogUrl("https://devdev.github.io/")
                .snsUrl("https://www.instagram.com/devdev")
                .build();

        userInfoDto = UserInfoDTO.builder()
                .id(1L)
                .email("dev@dev.com")
                .name("dev")
                .description("I'm Developer")
                .githubUrl("http://github.com/devdev")
                .blogUrl("https://devdev.github.io/")
                .snsUrl("https://www.instagram.com/devdev")
                .build();
    }

    @Test
    @DisplayName("존재하지 않는 회원")
    void getUserInfoFail() {
        // given
        Long id = 0L;

        // when
        ApiException exception = assertThrows(ApiException.class, () -> userService.getUserInfo(id));

        // then
        assertThat(ErrorCode.USER_NOT_FOUND).isEqualTo(exception.getErrorCode());
    }

    @Test
    void getUserInfoSuccess() {
        // given
        Long id = 1L;
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(modelMapper.map(any(User.class), any())).willReturn(userInfoDto);

        // when
        UserInfoDTO dto = userService.getUserInfo(id);

        // then
        assertThat(dto.getId()).isEqualTo(id);
        verify(userRepository).findById(any());

    }


    @Test
    @DisplayName("회원정보 수정")
    void updateUserInfo() {
        // given
        Long id = 1L;

        given(userRepository.getById(any())).willReturn(user);
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        // when
        userService.updateUserInfo(userInfoDto);

        // then
        Optional<User> getUser = userRepository.findById(id);
        assertThat(getUser.get().getId()).isEqualTo(id);

        verify(userRepository).getById(any());


    }

    @Test
    @DisplayName("프로필 출력")
    void getProfile() {
        // given
        Long id = 1L;
        ProfileDTO dto = ProfileDTO.builder()
                .id(id)
                .userId(id)
                .purpose("취업")
                .skills(List.of("JAVA", "SPRING"))
                .build();

        Profile entity = Profile.builder()
                .id(id)
                .purpose("취업")
                .user(User.builder().id(id).build())
                .build();

    }

    @Test
    void updateProfile() {
        // given
        Long id = 1L;
        ProfileDTO dto = ProfileDTO.builder()
                .id(id)
                .purpose("취업")
                .userId(id)
                .build();

        Profile entity = Profile.builder()
                .id(id)
                .purpose("취업")
                .user(User.builder().id(id).build())
                .build();

        given(profileRepository.getById(any())).willReturn(entity);
        given(profileRepository.findById(any())).willReturn(Optional.of(entity));

        // when
        userService.updateProfile(dto);

        // then
        Optional<Profile> profile = profileRepository.findById(id);
        assertThat(profile.get().getId()).isEqualTo(id);

        verify(profileRepository).getById(id);

    }

    @Test
    @DisplayName("유저보드 출력")
    void getUserBoard() {
        Long id = 1L;

        Profile profile = Profile.builder()
                .id(id)
                .purpose("취업")
                .user(user)
                .build();

        given(profileRepository.findByUserId(any())).willReturn(Optional.of(profile));

        // when
        UserBoardDTO userBoard = userService.getUserBoard(id);
        System.out.println(userBoard);

        // then
        assertThat(userBoard.getId()).isEqualTo(id);
        assertThat(userBoard.getPurpose()).isEqualTo("취업");
        verify(profileRepository).findByUserId(id);

    }
}