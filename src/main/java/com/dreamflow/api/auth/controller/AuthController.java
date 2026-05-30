package com.dreamflow.api.auth.controller;
import com.dreamflow.api.auth.dto.*;
import com.dreamflow.api.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request, HttpServletResponse servletResponse){
        LoginResponse response = authService.signUp(request);

        Cookie accessToken = new Cookie("access_token", response.accessToken());
        accessToken.setHttpOnly(true);
        accessToken.setSecure(true);
        accessToken.setPath("/");
        accessToken.setMaxAge(30*60);

        Cookie refreshToken = new Cookie("refresh_token", response.refreshToken());
        refreshToken.setHttpOnly(true);
        refreshToken.setSecure(true);
        refreshToken.setPath("/auth/refresh");
        refreshToken.setMaxAge(7*24*60*60);

        servletResponse.addCookie(accessToken);
        servletResponse.addCookie(refreshToken);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signin(@RequestBody LoginRequest request, HttpServletResponse servletResponse){
        LoginResponse response = authService.login(request);

        Cookie accessToken = new Cookie("access_token", response.accessToken());
        accessToken.setHttpOnly(true);
        accessToken.setSecure(true);
        accessToken.setPath("/");
        accessToken.setMaxAge(30*60);

        Cookie refreshToken = new Cookie("refresh_token", response.refreshToken());
        refreshToken.setHttpOnly(true);
        refreshToken.setSecure(true);
        refreshToken.setPath("/auth/refresh");
        refreshToken.setMaxAge(7*24*60*60);

        servletResponse.addCookie(accessToken);
        servletResponse.addCookie(refreshToken);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest request, HttpServletResponse response){
        RefreshResponse refreshResponse = authService.generateAccessToken(request.refreshToken());

        Cookie accessToken = new Cookie("access_token", refreshResponse.accessToken());
        accessToken.setHttpOnly(true);
        accessToken.setSecure(true);
        accessToken.setPath("/");
        accessToken.setMaxAge(30*60);

        response.addCookie(accessToken);

        return ResponseEntity.ok().build();
    }

}
