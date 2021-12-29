package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.entity.UserRole;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void register() {

        User user = User.builder()
                .email("user1@gmail.com")
                .password("1111")
                .nickname("test")
                .fromSocial(false)
                .role(UserRole.USER)
                .build();

        System.out.println(user.toString());

        String email = userService.register(userService.entityToDTO(user));
        log.info("email: " + email);
    }

    @Test
    public void login() {

    }
}
