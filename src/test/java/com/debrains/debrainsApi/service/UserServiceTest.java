package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.dto.UserInfoDTO;
import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void updateUserInfo() {
        // given
        Long id = 1L;

        UserInfoDTO dto = UserInfoDTO.builder()
                .id(id)
                .name("devdev")
                .description("I'm Developer")
                .githubUrl("http://github.com/devdev")
                .blogUrl(null)
                .snsUrl(null)
                .build();

        System.out.println(dto.toString());
        User entity = modelMapper.map(dto, User.class);
        System.out.println(entity);

        given(userRepository.getById(id)).willReturn(entity);
        given(userRepository.findById(id)).willReturn(Optional.of(entity));

        // when
        userService.updateUserInfo(dto);

        // then
        Optional<User> getUser = userRepository.findById(id);
//        assertThat(getUser.get().getId()).isEqualTo(id);

        verify(userRepository).getById(id);


    }
}