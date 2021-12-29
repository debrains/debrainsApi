package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.common.DefaultRes;
import com.debrains.debrainsApi.common.ResponseMessage;
import com.debrains.debrainsApi.common.StatusCode;
import com.debrains.debrainsApi.dto.EmailAuthDTO;
import com.debrains.debrainsApi.dto.UserDTO;
import com.debrains.debrainsApi.entity.EmailAuth;
import com.debrains.debrainsApi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserDTO userDTO) {

        log.info("UserDTO: " + userDTO.toString());

        String username = userService.register(userDTO);

        return new ResponseEntity(
                DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER, username),
                HttpStatus.OK);
    }

    /**
     * 이메일 인증
     */
    @GetMapping("/confirm-email")
    public ResponseEntity verifyAccount(@RequestBody EmailAuthDTO emailAuthDTO) {

        log.info("EmailAuthDTO: " + emailAuthDTO.toString());

        userService.confirmEmail(emailAuthDTO);

        return new ResponseEntity<>(
                DefaultRes.res(StatusCode.OK, ResponseMessage.EMAIL_CONFIRM),
                HttpStatus.OK);
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/reissued-email")
    public ResponseEntity reissuedEmail(@RequestBody EmailAuthDTO emailAuthDTO) {

        log.info("emailAuthDTO: " + emailAuthDTO);

        EmailAuth emailAuth = userService.issuedEmail(emailAuthDTO.getEmail());

        log.info("emailAuth: " + emailAuth);

        return new ResponseEntity<>(
                DefaultRes.res(StatusCode.CREATED, ResponseMessage.EMAIL_TOKEN, emailAuth),
                HttpStatus.OK);
    }

}
