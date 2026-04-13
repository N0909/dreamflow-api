package com.dreamflow.api.playlist.dto;

import java.time.LocalDateTime;

public record PlaylistResponse(int playlistId, String playlistName, LocalDateTime createdAt) {
}
