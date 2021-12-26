package com.debrains.debrainsApi.security;

import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.entity.UserRole;
import com.debrains.debrainsApi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class UserTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies() {

        // 1~80: USER
        // 81~90: USER, MANAGER
        // 91~100: USER, MANAGER, ADMIN

        IntStream.rangeClosed(1, 100).forEach(i -> {
            User user = User.builder()
                    .email("user" + i + "@gmail.com")
                    .name("user" + i)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();

            user.addUserRole(UserRole.USER);
            if (i > 80) {
                user.addUserRole(UserRole.MANAGER);
            }
            if (i > 90) {
                user.addUserRole(UserRole.ADMIN);
            }
            userRepository.save(user);
        });
    }

    @Test
    public void testRead(){

        Optional<User> result = userRepository.findByEmail("user95@gmail.com", false);

        User user = result.get();

        System.out.println(user);
    }
}

