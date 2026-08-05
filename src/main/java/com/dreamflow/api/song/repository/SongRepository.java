package com.dreamflow.api.song.repository;

import com.dreamflow.api.song.dto.SongDTO;
import com.dreamflow.api.song.entity.Song;
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

}
