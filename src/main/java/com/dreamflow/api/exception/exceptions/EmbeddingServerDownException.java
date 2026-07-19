package com.dreamflow.api.exception.exceptions;

public class EmbeddingServerDownException extends RuntimeException{
    public EmbeddingServerDownException(String message) {
        super(message);
    }
    public EmbeddingServerDownException(String message, Throwable cause){
        super(message, cause);
    }
}
