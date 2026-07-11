package com.dreamflow.api.song.repository;

import com.dreamflow.api.song.entity.SongMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongMetadataRepository extends JpaRepository<SongMetadata, Long> {
    Optional<SongMetadata> findBySongId(Integer songId);
}
