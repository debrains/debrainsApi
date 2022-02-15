package com.debrains.debrainsApi.common;

import com.debrains.debrainsApi.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithAuthUserSecurityFactory implements WithSecurityContextFactory<WithAuthUser> {

    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        Long id = annotation.id();
        String email = annotation.email();
        String role = annotation.role();
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        CustomUserDetails principal = new CustomUserDetails(id, email, authorities);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        return context;
    }
}
