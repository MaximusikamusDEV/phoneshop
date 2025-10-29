package com.es.phoneshop.web.exceptions;

public class EmptyOrderListException extends RuntimeException {
    public EmptyOrderListException(String message) {
        super(message);
    }
}
