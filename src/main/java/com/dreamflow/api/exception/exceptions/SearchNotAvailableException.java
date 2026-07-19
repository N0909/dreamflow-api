package com.dreamflow.api.exception.exceptions;

public class SearchNotAvailableException extends RuntimeException {
    public SearchNotAvailableException(String message){
        super(message);
    }
    public SearchNotAvailableException(String message, Throwable cause){
        super(message, cause);
    }
}


