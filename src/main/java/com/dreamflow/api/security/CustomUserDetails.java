package com.dreamflow.api.security;

import com.dreamflow.api.auth.entity.Role;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class CustomUserDetails implements UserDetails, Serializable {
    private int userId;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private Role role;

    public CustomUserDetails(int userId, String username, String email, String password,LocalDateTime createdAt, Role role){
        this.userId=userId;
        this.username = username;
        this.email=email;
        this.password=password;
        this.createdAt = createdAt;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_"+role.name())
        );
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public int getUserId(){
        return userId;
    }
    public String getName() { return username;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public Role getRole(){return role;}
}
