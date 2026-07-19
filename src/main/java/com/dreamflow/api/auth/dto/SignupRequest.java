package com.dreamflow.api.auth.dto;

import com.dreamflow.api.auth.entity.Role;

public record SignupRequest(String username, String email, String password, Role role) {
}
