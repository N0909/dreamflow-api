package com.dreamflow.api.playlist.repository;

import com.dreamflow.api.playlist.dto.PlaylistResponse;
import com.dreamflow.api.playlist.dto.PlaylistSongResponse;
import com.dreamflow.api.playlist.entity.PlaylistSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Integer> {
    List<PlaylistSong> findByPlaylist_PlaylistId(int playlistId);
}
