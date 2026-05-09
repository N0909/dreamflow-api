package com.dreamflow.api.home.controller;

import com.dreamflow.api.auth.entity.User;
import com.dreamflow.api.home.dto.HomeResponseDTO;
import com.dreamflow.api.home.service.HomeService;
import com.dreamflow.api.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @GetMapping
    public ResponseEntity<HomeResponseDTO> getHome(
            @AuthenticationPrincipal CustomUserDetails user){

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(homeService.getHomeData(user.getUserId()));
    }

}
