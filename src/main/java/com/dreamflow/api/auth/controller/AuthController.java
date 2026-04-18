package com.dreamflow.api.auth.controller;
import com.dreamflow.api.auth.dto.*;
import com.dreamflow.api.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<LoginResponse> signup(@RequestBody SignupRequest request){
        LoginResponse response = authService.signUp(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> signin(@RequestBody LoginRequest request){
        LoginResponse response = authService.login(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshToken(@RequestBody RefreshRequest request){
        RefreshResponse refreshResponse = authService.generateAccessToken(request.refreshToken());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(refreshResponse);
    }

}
