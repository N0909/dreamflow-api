package com.dreamflow.api.song.dto;

import java.io.Serializable;

public record SongDTO(int songId, String songName, long durationMs) implements Serializable {
}
