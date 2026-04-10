package com.dreamflow.api.playlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dreamflow.api.playlist.entity.Playlist;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
}
