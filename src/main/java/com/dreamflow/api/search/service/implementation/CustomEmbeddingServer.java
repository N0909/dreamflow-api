package com.dreamflow.api.search.service.implementation;
import com.dreamflow.api.search.dto.EmbeddingRequest;
import com.dreamflow.api.search.dto.EmbeddingResponse;
import com.dreamflow.api.search.service.EmbeddingService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CustomEmbeddingServer implements EmbeddingService {
    private final RestClient restClient;

    CustomEmbeddingServer(RestClient restClient){
        this.restClient = restClient;
    }

    @Override
    public float[] getEmbedding(EmbeddingRequest request) {
        EmbeddingResponse response = restClient.post()
                .uri("/generate-embedding")
                .body(request)
                .retrieve()
                .body(EmbeddingResponse.class);

        return response.embedding();
    }


}
