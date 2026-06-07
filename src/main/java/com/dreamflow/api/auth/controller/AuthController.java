package com.dreamflow.api.auth.controller;
import com.dreamflow.api.auth.dto.*;
import com.dreamflow.api.auth.service.AuthService;
import com.dreamflow.api.exception.exceptions.IllegalAuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Autowired
    private Environment env;

    private boolean isProduction() {
        return Arrays.asList(env.getActiveProfiles()).contains("prod");
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request, HttpServletResponse servletResponse){
        LoginResponse response = authService.signUp(request);

        Cookie accessToken = new Cookie("access_token", response.accessToken());
        accessToken.setHttpOnly(true);
        accessToken.setSecure(isProduction());
        accessToken.setPath("/");
        accessToken.setMaxAge(30*60);

        Cookie refreshToken = new Cookie("refresh_token", response.refreshToken());
        refreshToken.setHttpOnly(true);
        refreshToken.setSecure(isProduction());
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
        accessToken.setSecure(isProduction());
        accessToken.setPath("/");
        accessToken.setMaxAge(30*60);

        Cookie refreshToken = new Cookie("refresh_token", response.refreshToken());
        refreshToken.setHttpOnly(true);
        refreshToken.setSecure(isProduction());
        refreshToken.setPath("/auth/refresh");
        refreshToken.setMaxAge(7*24*60*60);

        servletResponse.addCookie(accessToken);
        servletResponse.addCookie(refreshToken);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response){

        Cookie[] cookies = request.getCookies();

        if (cookies==null){
            throw new IllegalAuthException("Authentication Expired");
        }

        String token = Arrays.stream(cookies).filter(cook->
                cook.getName().equals("refresh_token")
        )
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(()->
                new IllegalAuthException("Authentication Expired")
        );

        RefreshResponse refreshResponse = authService.generateAccessToken(token);

        Cookie accessToken = new Cookie("access_token", refreshResponse.accessToken());
        accessToken.setHttpOnly(true);
        accessToken.setSecure(isProduction());
        accessToken.setPath("/");
        accessToken.setMaxAge(30*60);

        response.addCookie(accessToken);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){
        Cookie accessToken = new Cookie("access_token", null);
        accessToken.setHttpOnly(true);
        accessToken.setSecure(isProduction());
        accessToken.setPath("/");
        accessToken.setMaxAge(0);

        Cookie refreshToken = new Cookie("refresh_token", null);
        refreshToken.setHttpOnly(true);
        refreshToken.setSecure(isProduction());
        refreshToken.setPath("/auth/refresh");
        refreshToken.setMaxAge(0);

        response.addCookie(accessToken);
        response.addCookie(refreshToken);

        return ResponseEntity.ok().build();
    }

}
