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
    @OneToOne(mappedBy = "song", orphanRemoval = true, cascade = CascadeType.ALL)
    private SongMetadata songMetadata;
    @Enumerated(EnumType.STRING)
    @Column(name="stream_status")
    private UploadStatus uploadStatus;
    @Column(name="job_id", unique=true, nullable = false)
    private String jobId;
    @Column(name="reason_for_failure")
    private String FailReason;
    @Enumerated(EnumType.STRING)
    @Column(name="visibility_status", nullable = false)
    private VisibilityStatus visibilityStatus;

    public void addPlaylistSong(PlaylistSong playlistSong){
        playlistSong.setSong(this);
        playlistSongList.add(playlistSong);
    }
}
