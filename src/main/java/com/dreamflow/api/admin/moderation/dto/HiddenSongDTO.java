package com.dreamflow.api.admin.moderation.dto;

import java.time.LocalDateTime;

public record HiddenSongDTO(
        int songId,
        String songName,
        long durationMs,
        LocalDateTime createdAt
) {
}
