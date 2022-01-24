package com.debrains.debrainsApi.service;

import com.debrains.debrainsApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

}
