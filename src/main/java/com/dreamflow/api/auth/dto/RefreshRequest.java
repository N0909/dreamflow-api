package com.dreamflow.api.auth.dto;

@Deprecated // since it's handled by http cookies
public record RefreshRequest(String refreshToken) {
}
