package com.dreamflow.api.search.service.implementation;
import co.elastic.clients.elasticsearch._types.KnnSearch;
import com.dreamflow.api.search.dto.EmbeddingRequest;
import com.dreamflow.api.search.entity.SongDocument;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.document.SearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import co.elastic.clients.elasticsearch._types.KnnQuery;

import org.springframework.stereotype.Service;
import com.dreamflow.api.search.service.EmbeddingService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class SongSearchService {
    private final EmbeddingService embeddingClient;
    private final ElasticsearchRepository elasticsearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public SongSearchService(EmbeddingService embeddingClient, ElasticsearchRepository elasticsearchRepository, ElasticsearchOperations elasticsearchOperations){
        this.embeddingClient = embeddingClient;
        this.elasticsearchRepository = elasticsearchRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<SongDocument> searchSong(String query){
        EmbeddingRequest request = new EmbeddingRequest(query);

        float[] embeddings = embeddingClient.getEmbedding(request);

        List<Float> floatList = IntStream
                                .range(0, embeddings.length)
                .mapToObj(i->embeddings[i]).collect(Collectors.toUnmodifiableList());

        KnnSearch knnSearch = KnnSearch.of(
                k-> k
                        .field("embedding")
                        .queryVector(floatList)
                        .k(5)
                        .numCandidates(50)
        );

        NativeQuery searchQuery = NativeQuery
                .builder()
                .withKnnSearches(knnSearch)
                .build();

        SearchHit<SongDocument> hits = elasticsearchOperations.search(
                searchQuery,
                SongDocument.class
        );

        return hits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();
    }
}
