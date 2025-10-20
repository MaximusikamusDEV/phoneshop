package com.es.phoneshop.web.exceptions;

public class InvalidOrderIdException extends RuntimeException {
    public InvalidOrderIdException(String message) {
        super(message);
    }
}
