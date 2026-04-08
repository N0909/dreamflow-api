package com.dreamflow.api.auth.dto;

import java.time.LocalDateTime;

public record UserResponse(int userid, String username, String email, LocalDateTime createdAt) {
}
