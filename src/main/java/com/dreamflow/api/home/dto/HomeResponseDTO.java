package com.dreamflow.api.home.dto;
import java.util.*;

import com.dreamflow.api.playlist.dto.PlaylistResponse;
import com.dreamflow.api.playlist.entity.Playlist;
import com.dreamflow.api.song.dto.SongDTO;
import com.dreamflow.api.song.entity.Song;
import org.springframework.data.domain.Page;

public record HomeResponseDTO(Page<SongDTO> songsList, List<PlaylistResponse> playlistList) {
}
