package com.dreamflow.api.auth.service;

import com.dreamflow.api.auth.dto.UserResponse;
import com.dreamflow.api.auth.entity.User;
import com.dreamflow.api.auth.repository.UserRepository;
import com.dreamflow.api.exception.exceptions.ResourceNotFoundException;
import com.dreamflow.api.security.CustomUserDetails;
import com.dreamflow.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserResponse getUser(){
        CustomUserDetails userDetails = (CustomUserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert userDetails != null;
        return new UserResponse(userDetails.getUserId(), userDetails.getName(), userDetails.getUsername(), userDetails.getRole(), userDetails.getCreatedAt());
    }
}
