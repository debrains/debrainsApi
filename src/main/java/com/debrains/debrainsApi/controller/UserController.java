package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.repository.UserRepository;
import com.debrains.debrainsApi.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserDetails user) {
        System.out.println(user);
        Long id = Long.valueOf(user.getUsername());
        return userRepository.findById(id).orElseThrow(() -> new IllegalStateException("n"));
    }


}
