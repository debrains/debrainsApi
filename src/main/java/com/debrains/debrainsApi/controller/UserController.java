package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.common.DefaultRes;
import com.debrains.debrainsApi.common.ResponseMessage;
import com.debrains.debrainsApi.common.StatusCode;
import com.debrains.debrainsApi.dto.UserDTO;
import com.debrains.debrainsApi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserDTO userDTO) {

        log.info("UserDTO: " + userDTO.toString());

        String username = userService.register(userDTO);

        return new ResponseEntity(
                DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER, username),
                HttpStatus.OK);
    }

}
