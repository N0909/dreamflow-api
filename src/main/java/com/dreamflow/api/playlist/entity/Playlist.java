package com.dreamflow.api.playlist.entity;
import com.dreamflow.api.auth.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="playlists")
@Data
@NoArgsConstructor
public class Playlist {
    @Id
    @Column(name="playlist_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int playlistId;
    @Column(name="playlist_name")
    private String playlistName;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @Column(name="created_at")
    private LocalDateTime createdAt;
}
