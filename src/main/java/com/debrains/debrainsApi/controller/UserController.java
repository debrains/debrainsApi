package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.common.ResponseCode;
import com.debrains.debrainsApi.dto.ResponseDTO;
import com.debrains.debrainsApi.dto.UserInfoDTO;
import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.exception.ApiException;
import com.debrains.debrainsApi.repository.UserRepository;
import com.debrains.debrainsApi.security.CurrentUser;
import com.debrains.debrainsApi.security.CustomUserDetails;
import com.debrains.debrainsApi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
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
        ResponseDTO<UserInfoDTO> response = ResponseDTO.<UserInfoDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
//                .data(userInfo)
                .build();
        return null;
    }

    @GetMapping("/validateName")
    public boolean validateName(String name) {
        return userRepository.existsByName(name);
    }


}
