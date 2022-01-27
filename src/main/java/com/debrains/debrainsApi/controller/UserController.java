package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.dto.UserInfoDTO;
import com.debrains.debrainsApi.repository.UserRepository;
import com.debrains.debrainsApi.security.CurrentUser;
import com.debrains.debrainsApi.security.CustomUserDetails;
import com.debrains.debrainsApi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user", produces = MediaTypes.HAL_JSON_VALUE)
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/info")
    public ResponseEntity<UserInfoDTO> getUserInfo(@CurrentUser CustomUserDetails user) {
        UserInfoDTO userInfo = userService.getUserInfo(user.getId());
        return null;
    }

    @PostMapping("/info")
    public void saveUserInfo(@RequestBody @Validated UserInfoDTO dto) {
        userService.updateUserInfo(dto);
    }

    @GetMapping("/validateName")
    public boolean validateName(String name) {
        return userRepository.existsByName(name);
    }


}
