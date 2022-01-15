package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.repository.UserRepository;
import com.debrains.debrainsApi.security.CustomUserDetails;
import com.debrains.debrainsApi.security.jwt.JwtTokenProvider;
import com.debrains.debrainsApi.security.jwt.TokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    public TokenDTO refreshToken(TokenDTO oldToken) {
        // Validation Refresh Token
        if (!tokenProvider.validateToken(oldToken.getRefreshToken())) {
            throw new RuntimeException("Not Validated Refresh Token");
        }

        Authentication authentication = tokenProvider.getAuthentication(oldToken.getAccessToken());
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        Long id = Long.valueOf(user.getName());
        String savedToken = userRepository.getRefreshTokenById(id);

        if (!savedToken.equals(oldToken.getRefreshToken())) {
            throw new RuntimeException("Not Matched Refresh Token");
        }

        TokenDTO newToken = tokenProvider.createToken(authentication);
        userRepository.updateRefreshToken(id, newToken.getRefreshToken());

        return newToken;
    }
}
