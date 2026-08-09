package com.dreamflow.api.admin.moderation.dto;

import com.dreamflow.api.song.entity.VisibilityStatus;

public record ModerationResponseDTO(int songId, String songName, VisibilityStatus visibilityStatus) {
}
