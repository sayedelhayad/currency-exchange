package com.ce.auth.config;

import com.ce.auth.repository.UserRepository;
import com.ce.auth.dto.User;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenProvider tokenService;
    private final UserRepository userRepository;
    private final Gson gson;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null) {
            User user = tokenService.validateToken(token);
            var userEntity = userRepository.findByUsername(user.getUsername());
            var authentication = new UsernamePasswordAuthenticationToken(userEntity, null, userEntity.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.setAttribute("user", gson.toJson(user));
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;
        return authHeader.replace("Bearer ", "");
    }
}