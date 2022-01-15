package com.debrains.debrainsApi.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
}
