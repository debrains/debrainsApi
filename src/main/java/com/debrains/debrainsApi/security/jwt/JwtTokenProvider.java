package com.debrains.debrainsApi.security.jwt;

import com.debrains.debrainsApi.repository.UserRepository;
import com.debrains.debrainsApi.security.CustomUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Log4j2
@Component
public class JwtTokenProvider {

    private final String SECRET_KEY;
    private final String COOKIE_REFRESH_TOKEN_KEY;
    private final Long ACCESS_TOKEN_EXPIRE_LENGTH = 1000L * 60 * 60;
    private final Long REFRESH_TOKEN_EXPIRE_LENGTH = 1000L * 60 * 60 * 24 * 7;
    private final String AUTHORITIES_KEY = "role";

    @Autowired
    private UserRepository userRepository;

    public JwtTokenProvider(@Value("${app.auth.token.secret-key}")String secretKey, @Value("${app.auth.token.refresh-cookie-key}")String cookieKey) {
        this.SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.COOKIE_REFRESH_TOKEN_KEY = cookieKey;
    }

    public String createAccessToken(Authentication authentication) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH);

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        String userId = user.getName();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(userId)
                .claim(AUTHORITIES_KEY, role)
                .setIssuer("debrains")
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();
    }

    public void createRefreshToken(Authentication authentication, HttpServletResponse response) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_LENGTH);

        String refreshToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setIssuer("debrains")
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();

        saveRefreshToken(authentication, refreshToken);

        ResponseCookie cookie = ResponseCookie.from(COOKIE_REFRESH_TOKEN_KEY, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .maxAge(REFRESH_TOKEN_EXPIRE_LENGTH/1000)
                .path("/")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void saveRefreshToken(Authentication authentication, String refreshToken) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Long id = Long.valueOf(user.getName());

        userRepository.updateRefreshToken(id, refreshToken);
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        CustomUserDetails principal = new CustomUserDetails(Long.valueOf(claims.getSubject()), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalStateException e) {
            log.info("JWT 토큰이 잘못되었습니다");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
