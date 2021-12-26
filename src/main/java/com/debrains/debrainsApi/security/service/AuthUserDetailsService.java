package com.debrains.debrainsApi.security.service;

import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.repository.UserRepository;
import com.debrains.debrainsApi.security.dto.AuthUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("SecurityUserDetailsService loadUserByUsername: " + username);

        Optional<User> result = userRepository.findByEmail(username, false);

        if (result.isEmpty()) {
            throw new UsernameNotFoundException("Check Email or Social");
        }

        User user = result.get();

        log.info("-------------------------");
        log.info(user);

        AuthUserDTO authUser = new AuthUserDTO(
                user.getEmail(),
                user.getPassword(),
                user.isFromSocial(),
                user.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toSet())
        );

        authUser.setName(user.getName());

        return authUser;
    }
}
