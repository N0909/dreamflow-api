package com.dreamflow.api.admin.moderation.dto;

import com.dreamflow.api.song.entity.UploadStatus;
import com.dreamflow.api.song.entity.VisibilityStatus;

import java.time.LocalDateTime;

public record PendingSongDTO(
        int songId,
        String songName,
        long durationMs,
        LocalDateTime createdAt) {
}
