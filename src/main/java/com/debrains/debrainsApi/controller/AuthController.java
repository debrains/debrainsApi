package com.debrains.debrainsApi.controller;

import com.debrains.debrainsApi.repository.UserRepository;
import com.debrains.debrainsApi.security.jwt.JwtTokenProvider;
import com.debrains.debrainsApi.security.jwt.TokenDTO;
import com.debrains.debrainsApi.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity refreshToken(@RequestBody TokenDTO oldToken) {
        return ResponseEntity.ok().body(authService.refreshToken(oldToken));
    }

}
