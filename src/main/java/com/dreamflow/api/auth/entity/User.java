package com.dreamflow.api.auth.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(name="username")
    private String username;
    @Column(name="email")
    private String email;
    @Column(name="password_hash")
    private String password;
    @Column(name="created_at")
    private LocalDateTime createdAt;
}
