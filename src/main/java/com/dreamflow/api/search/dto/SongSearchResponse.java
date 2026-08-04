package com.dreamflow.api.search.dto;
import java.util.List;

import com.dreamflow.api.song.dto.SongDTO;

public record SongSearchResponse(String query, List<SongDTO> searchResults) {}
