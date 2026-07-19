package com.dreamflow.api.playlist.repository;

import com.dreamflow.api.playlist.dto.PlaylistResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dreamflow.api.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    @Query("SELECT new com.dreamflow.api.playlist.dto.PlaylistResponse(p.playlistId, p.playlistName, p.createdAt) FROM Playlist p WHERE p.user.userId=:userId")
    List<PlaylistResponse> getAllPlaylists(int userId);
    @Query("SELECT new com.dreamflow.api.playlist.dto.PlaylistResponse(p.playlistId,p.playlistName, p.createdAt) FROM Playlist p WHERE p.user.userId=:userId  ORDER BY p.createdAt DESC")
    List<PlaylistResponse> findTopNRecent(int userId, Pageable pageable);
    Optional<Playlist> findByPlaylistIdAndUser_UserId(int playlistId, int userId);
    boolean existsByPlaylistNameAndUser_UserId(
            String playlistName,
            int userId
    );
}
