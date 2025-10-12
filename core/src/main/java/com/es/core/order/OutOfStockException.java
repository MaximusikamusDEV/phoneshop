package com.es.core.order;

public class OutOfStockException extends Exception {
    private Long phoneId;

    public OutOfStockException(Long phoneId) {
        this.phoneId = phoneId;
    }

    public OutOfStockException(String message, Long phoneId) {
        super(message);
        this.phoneId = phoneId;
    }

    public Long getPhoneId() {
        return phoneId;
    }
}
