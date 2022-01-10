package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.common.ResponseCode;
import com.debrains.debrainsApi.dto.ResponseDTO;
import com.debrains.debrainsApi.dto.UserDTO;
import com.debrains.debrainsApi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;


}
