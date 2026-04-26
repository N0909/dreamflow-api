package com.dreamflow.api.auth.service;

import com.dreamflow.api.auth.dto.*;
import com.dreamflow.api.auth.entity.User;
import com.dreamflow.api.auth.repository.UserRepository;
import com.dreamflow.api.exception.exceptions.IllegalAuthException;
import com.dreamflow.api.exception.exceptions.IllegalTokenException;
import com.dreamflow.api.security.CustomUserDetails;
import com.dreamflow.api.security.CustomeUserDetailsService;
import com.dreamflow.api.security.JwtService;
import io.jsonwebtoken.security.SignatureException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    private final CustomeUserDetailsService userDetailsService;
    private static final String REFRESH = "refresh";
    private static final String ACCESS = "access";

    @Transactional
    public LoginResponse signUp(SignupRequest input){
        if(userRepository.existsByEmail(input.email())){
            throw new IllegalAuthException("Email already in use");
        }

        User user = new User();
        user.setUsername(input.username());
        user.setEmail(input.email());
        user.setPassword(passwordEncoder.encode(input.password()));
        user.setCreatedAt(LocalDateTime.now());

        User createdUser = userRepository.save(user);

        Map<String, Object> claims = Map.of(
                "userId", createdUser.getUserId(),
                "type",ACCESS
        );

        Map<String, Object> refreshClaims = Map.of(
                "userId", createdUser.getUserId(),
                "type", REFRESH
        );

        String accessToken = jwtService.generateToken(claims, createdUser.getEmail(), 15*60*1000);

        String refreshToken = jwtService.generateToken(refreshClaims, createdUser.getEmail(), 7*24*60*60*1000);

        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse login(LoginRequest input) {
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.email(), input.password())
            );
        }catch(AuthenticationException ex){
            throw new IllegalAuthException("Bad Credentials");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserDetails userDetails)){
            throw new IllegalAuthException("Invalid authentication principal");
        }

        Map<String, Object> claims = Map.of(
                "userId", userDetails.getUserId(),
                "type",ACCESS
        );

        String accessToken = jwtService.generateToken(claims, userDetails.getUsername(), 15*60*1000);

        Map<String, Object> refreshClaims = Map.of(
                "userId", userDetails.getUserId(),
                "type", REFRESH
        );

        String refreshToken = jwtService.generateToken(refreshClaims, userDetails.getUsername(), 7*24*60*60*1000);

        return new LoginResponse(accessToken, refreshToken);
    }

    public RefreshResponse generateAccessToken(String refreshToken){
        String type;
        try{
             type = jwtService.extractClaim(refreshToken, claims -> claims.get("type", String.class));
        }catch(SignatureException ex){
            throw new IllegalTokenException("Token is Invalid");
        }

        if (!type.equals(REFRESH)){
            throw new IllegalTokenException("Not a Refresh Token");
        }

        String email = jwtService.extractUsername(refreshToken);

        int userId = jwtService.extractClaim(refreshToken, claims -> claims.get("userId", Integer.class));

        Map<String, Object> claims = Map.of(
                "userId", userId,
                "type", "access"
        );

        String accessToken = jwtService.generateToken(claims, email, 15*60*1000);

        return new RefreshResponse(accessToken);
    }
}
