package com.dreamflow.api.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails, Serializable {
    private int userId;
    private String email;
    private String password;

    public CustomUserDetails(int userId, String email, String password){
        this.userId=userId;
        this.email=email;
        this.password=password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
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

}
