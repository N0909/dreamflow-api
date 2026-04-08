package com.dreamflow.api.auth.controller;

import com.dreamflow.api.auth.dto.UserResponse;
import com.dreamflow.api.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<UserResponse> getUser(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        UserResponse response = userService.getUser(token);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
