package com.dreamflow.api.search.service;

import com.dreamflow.api.search.dto.EmbeddingRequest;
import com.dreamflow.api.search.dto.EmbeddingResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmbeddingServiceTest {
    @Autowired
    private EmbeddingService embeddingService;

    @Test
    public void testGetEmbedding(){
        EmbeddingRequest request = new EmbeddingRequest("Hello world");
        float[] embedding = embeddingService.getEmbedding(request);
        System.out.println(embedding.length);
    }
}
