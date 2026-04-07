package com.dreamflow.api.song.repository;

import com.dreamflow.api.song.dto.SongDTO;
import com.dreamflow.api.song.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {
    @Query("SELECT new com.dreamflow.api.song.dto.SongDTO(s.songId, s.songName, s.durationMs) FROM Song s")
    List<SongDTO> findSongs();
    @Query("SELECT new com.dreamflow.api.song.dto.SongDTO(s.songId, s.songName, s.durationMs) FROM Song s WHERE s.songId=:songId")
    Optional<SongDTO> findSongById(int songId);
}
