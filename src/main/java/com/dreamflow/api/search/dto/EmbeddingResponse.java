package com.dreamflow.api.search.dto;

public record EmbeddingResponse(String text, float[] embedding) {
}
