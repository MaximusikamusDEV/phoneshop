package com.es.core.cart.exceptions;

public class ItemNotExistException extends RuntimeException {
    public ItemNotExistException(String message) {
        super(message);
    }
}
