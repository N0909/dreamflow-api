package com.dreamflow.api.song.entity;
import com.dreamflow.api.playlist.entity.PlaylistSong;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="songs")
@Data
@NoArgsConstructor
public class Song {
    @Id
    @Column(name="song_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int songId;
    @Column(name="song_name")
    private String songName;
    @Column(name="song_path")
    private String songPath;
    @Column(name="duration_ms")
    private long durationMs;
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "song", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<PlaylistSong> playlistSongList = new ArrayList<>();

    public void addPlaylistSong(PlaylistSong playlistSong){
        playlistSong.setSong(this);
        playlistSongList.add(playlistSong);
    }
}
