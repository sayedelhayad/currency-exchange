package com.ce.auth.controller;

import com.ce.auth.repository.UserEntity;
import com.ce.auth.dto.JwtDto;
import com.ce.auth.dto.SignInDto;
import com.ce.auth.dto.SignUpDto;
import com.ce.auth.dto.User;
import com.ce.auth.service.AuthService;
import com.ce.auth.config.TokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthService service;
    private final TokenProvider tokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpDto data) {
        service.signUp(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtDto> signIn(@RequestBody @Valid SignInDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        UserEntity authUser = (UserEntity) authenticationManager.authenticate(usernamePassword).getPrincipal();
        User user = User.builder()
                .username(authUser.getUsername())
                .type(authUser.getType())
                .joiningDate(authUser.getJoiningDate())
                .tenure(getUserTenure(authUser))
                .build();
        var accessToken = tokenService.generateAccessToken(user);
        return ResponseEntity.ok(new JwtDto(accessToken));
    }

    private int getUserTenure(final UserEntity userEntity) {
        long diff = new Date().getTime() - userEntity.getJoiningDate().getTime();
        int diffYears = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) / 365;

        return diffYears;
    }

}