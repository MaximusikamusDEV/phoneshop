package com.es.core.cart.exceptions;

public class OutOfStockException extends RuntimeException {
    private Long phoneId;
    private Integer stock;

    public OutOfStockException(Long phoneId, Integer stock) {
        this.phoneId = phoneId;
        this.stock = stock;
    }

    public OutOfStockException(String message) {
        super(message);
    }

    public OutOfStockException(String message, Long phoneId, Integer stock) {
        super(message);
        this.phoneId = phoneId;
        this.stock = stock;
    }

    public OutOfStockException(String message, Integer stock) {
        super(message);
        this.stock = stock;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public Integer getStock(){
        return stock;
    }
}
