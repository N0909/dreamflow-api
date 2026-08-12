package com.dreamflow.api.admin.users.dto;

import com.dreamflow.api.auth.entity.Role;

import java.time.LocalDateTime;

public record AdminUserDTO(int userId, String username, String email, Role role, LocalDateTime createdAt) {
}
