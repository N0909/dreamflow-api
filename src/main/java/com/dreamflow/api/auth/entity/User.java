package com.dreamflow.api.auth.entity;
import com.dreamflow.api.playlist.entity.Playlist;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(name="username")
    private String username;
    @Column(name="email", unique = true)
    private String email;
    @Column(name="password_hash")
    private String password;
    @Column(name="created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Playlist> playlists = new ArrayList<>();

    public void addPlaylist(Playlist playlist){
        playlist.setUser(this);
        playlists.add(playlist);
    }
}
