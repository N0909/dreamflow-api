package com.dreamflow.api.song.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="songs_metadata")
@Data
public class SongMetadata {
    @Id
    @Column(name="song_metadata_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long songMetadataId;
    @Column(name="song_tags")
    private String songTags;
    @Column(name="song_lyrics")
    private String songLyrics;
    @Column(name="song_poster")
    private String songPoster;
    @Column(name="song_lyrics_synced")
    private String songLyricsSynced;
    @Column(name="songGenre")
    private String songGenre;
    @Column(name="added_at")
    @CreationTimestamp
    private LocalDateTime addedAt;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="song_id")
    private Song song;
}
