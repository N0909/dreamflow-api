package com.dreamflow.api.search.elastic.provider;

import com.dreamflow.api.exception.exceptions.EmbeddingServerDownException;
import com.dreamflow.api.exception.exceptions.SearchNotAvailableException;
import com.dreamflow.api.search.dto.EmbeddingRequest;
import com.dreamflow.api.search.dto.SongSearchResponse;
import com.dreamflow.api.search.elastic.entity.SongDocument;
import com.dreamflow.api.search.elastic.embedding.EmbeddingService;
import com.dreamflow.api.search.service.SearchProvider;
import com.dreamflow.api.song.dto.SongDTO;
import com.dreamflow.api.song.repository.SongRepository;

import co.elastic.clients.elasticsearch._types.KnnSearch;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Primary
public class ElasticSearchProvider implements SearchProvider{
    private final EmbeddingService embeddingClient;
    private final ElasticsearchOperations elasticsearchOperations;
    private final SongRepository songRepository;

    public ElasticSearchProvider(EmbeddingService embeddingClient,
            ElasticsearchOperations elasticsearchOperations, SongRepository songRepository) {
        this.embeddingClient = embeddingClient;
        this.elasticsearchOperations = elasticsearchOperations;
        this.songRepository = songRepository;
    }

    public SongSearchResponse search(String query) {
        try {
            EmbeddingRequest request = new EmbeddingRequest(query);

            float[] embeddings = embeddingClient.getEmbedding(request);

            List<Float> floatList = IntStream
                    .range(0, embeddings.length)
                    .mapToObj(i -> embeddings[i]).collect(Collectors.toUnmodifiableList());

            KnnSearch knnSearch = KnnSearch.of(
                    k -> k
                            .field("embedding")
                            .queryVector(floatList)
                            .k(10)
                            .numCandidates(25));

            Query keywordQuery = Query.of(q -> q.multiMatch(
                    m -> m.query(query)
                            .fields(
                                    "song_name^3",
                                    "genre^2",
                                    "tags^2",
                                    "lyrics^2")));

            NativeQuery searchQuery = NativeQuery
                    .builder()
                    .withQuery(keywordQuery)
                    .withKnnSearches(knnSearch)
                    .build();

            SearchHits<SongDocument> hits = elasticsearchOperations.search(
                    searchQuery,
                    SongDocument.class);

            List<Integer> songIds = hits
                                    .getSearchHits()
                                    .stream()
                                    .map(hit -> hit.getContent().getSongId())
                                    .toList();

            List<SongDTO> songs =  songRepository.findSongs(songIds);

            return new SongSearchResponse(query, songs);
        } catch (EmbeddingServerDownException e) {
            throw new SearchNotAvailableException(
                    "Search is temporarily unavailable. Please try again later.", e);
        }
    }
}