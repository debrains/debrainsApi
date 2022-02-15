package com.debrains.debrainsApi.entity;

import com.debrains.debrainsApi.dto.user.ProfileDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class ProfileTest {

    @Test
    @DisplayName("프로필 변경")
    void updateProfile() {
        // given
        Profile profile = Profile.builder()
                .id(1L)
                .purpose("취업")
                .skills("JAVA,SPRING,MYSQL")
                .build();
        ProfileDTO dto = ProfileDTO.builder()
                .id(1L)
                .userId(1L)
                .purpose("이직")
                .skills(List.of("PYTHON"))
                .build();

        // when
        profile.updateProfile(dto);

        // then
        assertThat(profile.getPurpose()).isEqualTo("이직");
        assertThat(profile.getSkills()).isEqualTo("PYTHON");
    }
}