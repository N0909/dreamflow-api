package com.dreamflow.api.auth.dto;

public record LoginResponse(String accessToken, String refreshToken) {
}
