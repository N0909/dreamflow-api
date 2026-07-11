package com.dreamflow.api.search.repository;

import com.dreamflow.api.search.entity.SongDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongSearchRepository extends ElasticsearchRepository<SongDocument, Integer> {
}
