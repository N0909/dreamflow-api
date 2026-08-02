package com.dreamflow.api.search.service.implementation;

import com.dreamflow.api.song.dto.*;
import com.dreamflow.api.song.repository.SongRepository;
import com.dreamflow.api.search.dto.SongSearchResponse;
import com.dreamflow.api.search.service.SearchProvider;
import java.util.*;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
public class DatabaseSearchProvider implements SearchProvider{

    private final SongRepository songRepository;

    public DatabaseSearchProvider(SongRepository songRepository){
        this.songRepository = songRepository;
    }

    @Override
    public SongSearchResponse search(String query){
        return new SongSearchResponse(query, songRepository.search(query));
    }
}
