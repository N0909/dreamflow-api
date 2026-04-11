package com.dreamflow.api.playlist.dto;

public record SongAddedResponse(int playlistId, String playlistName, int SongId, String songName) {
}
