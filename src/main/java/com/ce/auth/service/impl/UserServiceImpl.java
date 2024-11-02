package com.ce.auth.service.impl;

import com.ce.auth.repository.UserRepository;
import com.ce.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails getUserByUsername(String username) throws Exception {
        UserDetails user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("UserEntity not found");
        }
        return user;
    }
}
