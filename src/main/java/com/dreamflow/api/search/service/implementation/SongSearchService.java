package com.dreamflow.api.search.service.implementation;
import com.dreamflow.api.search.elastic.provider.ElasticSearchProvider;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

import com.dreamflow.api.search.dto.SongSearchResponse;
import com.dreamflow.api.exception.exceptions.*;

@Service
public class SongSearchService {
    private final ElasticSearchProvider elasticSearchProvider;
    private final DatabaseSearchProvider databaseSearchProvider;

    public SongSearchService(ElasticSearchProvider elasticSearchProvider, DatabaseSearchProvider databaseSearchProvider){
        this.elasticSearchProvider = elasticSearchProvider;
        this.databaseSearchProvider = databaseSearchProvider;
    }

    public SongSearchResponse searchSong(String query){
        try{
            return elasticSearchProvider.search(query);
        }
        catch(DataAccessResourceFailureException | SearchNotAvailableException exception){
            return databaseSearchProvider.search(query);
        }
    }
}