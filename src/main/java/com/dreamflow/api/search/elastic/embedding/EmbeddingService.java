package com.dreamflow.api.search.elastic.embedding;
import com.dreamflow.api.search.dto.EmbeddingRequest;
import org.springframework.stereotype.Service;

@Service
public interface EmbeddingService {
    float[] getEmbedding(EmbeddingRequest request);
}
