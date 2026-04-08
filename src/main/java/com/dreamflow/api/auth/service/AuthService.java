package com.dreamflow.api.auth.service;

import com.dreamflow.api.auth.dto.*;
import com.dreamflow.api.auth.entity.User;
import com.dreamflow.api.auth.repository.UserRepository;
import com.dreamflow.api.security.CustomUserDetails;
import com.dreamflow.api.security.CustomerUserDetailsService;
import com.dreamflow.api.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomerUserDetailsService userDetailsService;

    @Transactional
    public LoginResponse signUp(SignupRequest input){
        if(userRepository.existsByEmail(input.email())){
            throw new IllegalStateException("Email already in use");
        }

        User user = new User();
        user.setUsername(input.username());
        user.setEmail(input.email());
        user.setPassword(passwordEncoder.encode(input.password()));
        user.setCreatedAt(LocalDateTime.now());

        User createdUser = userRepository.save(user);

        Map<String, Object> claims = Map.of(
                "userId", createdUser.getUserId(),
                "type","access"
        );

        Map<String, Object> refreshClaims = Map.of("type", "refresh");

        String accessToken = jwtService.generateToken(claims, createdUser.getEmail(), 15*60*1000);

        String refreshToken = jwtService.generateToken(refreshClaims, createdUser.getEmail(), 7*24*60*60*1000);

        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse login(LoginRequest input) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.email(), input.password())
        );

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserDetails userDetails)){
            throw new IllegalStateException("Invalid authentication principal");
        }

        Map<String, Object> claims = Map.of(
                "userId", userDetails.getUserId(),
                "type","access"
        );

        String accessToken = jwtService.generateToken(claims, userDetails.getEmail(), 15*60*1000);

        Map<String, Object> refreshClaims = Map.of("type", "refresh");

        String refreshToken = jwtService.generateToken(refreshClaims, userDetails.getEmail(), 7*24*60*60*1000);

        return new LoginResponse(accessToken, refreshToken);
    }
}
