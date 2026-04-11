package com.dreamflow.api.playlist.entity;
import com.dreamflow.api.song.entity.Song;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="playlist_songs")
@Data
@NoArgsConstructor
public class PlaylistSong {
    @Id
    @Column(name="playlist_song_id")
    private int playlistSongId;
    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;
    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;
    @CreationTimestamp
    @Column(name="added_at")
    private LocalDateTime addedAt;
}
