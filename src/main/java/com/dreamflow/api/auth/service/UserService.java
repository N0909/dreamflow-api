package com.dreamflow.api.auth.service;

import com.dreamflow.api.auth.dto.UserResponse;
import com.dreamflow.api.auth.entity.User;
import com.dreamflow.api.auth.repository.UserRepository;
import com.dreamflow.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserResponse getUser(String accessToken){
        int userId = jwtService.extractClaim(accessToken, claims -> claims.get("userId", Integer.class));

        User user = userRepository.findById(userId).orElseThrow(()->new IllegalStateException("User with id: "+userId+" doesn't exist"));

        return new UserResponse(user.getUserId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
    }
}
