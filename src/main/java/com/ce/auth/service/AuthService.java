package com.ce.auth.service;

import com.ce.auth.repository.UserEntity;
import com.ce.auth.repository.UserRepository;
import com.ce.auth.exception.InvalidJwtException;
import com.ce.auth.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = repository.findByUsername(username);
        return user;
    }

    public UserDetails signUp(SignUpDto data) throws InvalidJwtException {

        final UserDetails user = repository.findByUsername(data.username());
        if (user != null) {
            throw new InvalidJwtException("Username already exists");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserEntity newUserEntity = new UserEntity(data.username(), encryptedPassword, data.type(), data.joiningDate());
        return repository.save(newUserEntity);
    }
}