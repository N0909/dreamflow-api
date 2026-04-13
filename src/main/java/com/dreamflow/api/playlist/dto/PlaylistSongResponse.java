package com.dreamflow.api.playlist.dto;

import com.dreamflow.api.song.dto.SongDTO;

import java.time.LocalDateTime;
import java.util.List;

public record PlaylistSongResponse(int playlistId, String playlistName, LocalDateTime createdAt, List<SongDTO> songs) {
}
