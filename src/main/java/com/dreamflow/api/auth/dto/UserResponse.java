package com.dreamflow.api.auth.dto;

import com.dreamflow.api.auth.entity.Role;

import java.time.LocalDateTime;

public record UserResponse(int userid, String username, String email, Role role, LocalDateTime createdAt) {
}
