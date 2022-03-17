package com.debrains.debrainsApi.entity;

import com.debrains.debrainsApi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserTest {

    @Test
    void calExp() {
        // given
        User user = User.builder()
                .id(1L)
                .email("dev@dev.com")
                .tier(1)
                .exp(0L)
                .build();

        // when
        user.calExp(501L);

        // then
        assertThat(user.getTier()).isEqualTo(2);
    }
}