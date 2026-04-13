package com.dreamflow.api.playlist.repository;

import com.dreamflow.api.playlist.dto.PlaylistResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dreamflow.api.playlist.entity.Playlist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    @Query("SELECT new com.dreamflow.api.playlist.dto.PlaylistResponse(p.playlistId, p.playlistName, p.createdAt) FROM Playlist p WHERE p.user.userId=:userId")
    List<PlaylistResponse> getAllPlaylists(int userId);
}
