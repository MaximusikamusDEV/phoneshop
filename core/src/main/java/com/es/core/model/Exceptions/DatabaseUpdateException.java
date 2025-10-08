package com.es.core.model.Exceptions;

public class DatabaseUpdateException extends RuntimeException {
    public DatabaseUpdateException(String message) {
        super(message);
    }
}
