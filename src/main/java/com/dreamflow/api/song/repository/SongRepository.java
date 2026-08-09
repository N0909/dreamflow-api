package com.dreamflow.api.song.repository;

import com.dreamflow.api.admin.moderation.dto.HiddenSongDTO;
import com.dreamflow.api.admin.moderation.dto.PendingSongDTO;
import com.dreamflow.api.song.dto.SongDTO;
import com.dreamflow.api.song.entity.Song;
import com.dreamflow.api.song.entity.UploadStatus;
import com.dreamflow.api.song.entity.VisibilityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {
    @Query("SELECT new com.dreamflow.api.song.dto.SongDTO(s.songId, s.songName, s.durationMs) FROM Song s WHERE s.uploadStatus='COMPLETED' AND s.visibilityStatus='APPROVED'")
    Page<SongDTO> findSongs(Pageable pageable);

    @Query("SELECT new com.dreamflow.api.song.dto.SongDTO(s.songId, s.songName, s.durationMs) FROM Song s WHERE s.songId=:songId AND s.uploadStatus='COMPLETED' AND s.visibilityStatus='APPROVED'")
    Optional<SongDTO> findSongById(int songId);
    Optional<Song> findByJobId(String jobId);
    @Query("""
                SELECT new com.dreamflow.api.song.dto.SongDTO(
                    s.songId,
                    s.songName,
                    s.durationMs
                )
                FROM Song s
                WHERE s.songId IN :ids
                            AND s.visibilityStatus='ACCEPTED' AND s.uploadStatus='COMPLTED'
            """)
    List<SongDTO> findSongs(@Param("ids") List<Integer> ids);
    @Query("""
            SELECT new com.dreamflow.api.song.dto.SongDTO(
                s.songId,
                s.songName,
                s.durationMs
            )
            FROM Song s
            JOIN s.songMetadata sm
            WHERE LOWER(
                CONCAT(
                    CONCAT(
                        CONCAT(
                            CONCAT(
                                CONCAT(s.songName, ' '),
                                COALESCE(sm.songTags, '')
                            ),
                            CONCAT(' ', COALESCE(sm.songLyrics, ''))
                        ),
                        CONCAT(' ', COALESCE(sm.songGenre, ''))
                    ),
                    ''
                )
            ) LIKE LOWER(CONCAT('%', :query, '%'))
                        AND s.uploadStatus='COMPLETED' AND s.visibilityStatus='APPROVED' 
            """)
    List<SongDTO> search(String query);
    @Query("SELECT new com.dreamflow.api.admin.moderation.dto.PendingSongDTO(s.songId, s.songName, s.durationMs, s.createdAt) FROM Song s WHERE s.visibilityStatus='PENDING' AND s.uploadStatus='COMPLETED'")
    List<PendingSongDTO> findAllPendingViewStatus();
    @Query("SELECT new com.dreamflow.api.admin.moderation.dto.HiddenSongDTO(s.songId, s.songName, s.durationMs, s.createdAt) FROM Song s WHERE s.visibilityStatus='HIDDEN' AND s.uploadStatus='COMPLETED'")
    List<HiddenSongDTO> findAllHiddenViewStatus();
    Optional<Song> findBySongIdAndUploadStatusAndVisibilityStatus(
            int songId,
            UploadStatus uploadStatus,
            VisibilityStatus visibilityStatus
    );
    Optional<Song> findBySongId(int songId);
    Optional<Song> findBySongIdAndUploadStatus(
            int songId,
            UploadStatus uploadStatus
    );
}
