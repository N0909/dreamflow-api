package com.dreamflow.api.search.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dreamflow.api.search.dto.SongSearchResponse;
import com.dreamflow.api.song.dto.*;


@Service
public interface SearchProvider{
    SongSearchResponse search(String query);
}