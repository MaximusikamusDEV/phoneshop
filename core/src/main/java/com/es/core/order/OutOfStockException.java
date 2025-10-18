package com.es.core.order;

public class OutOfStockException extends Exception {
    private Long phoneId;
    private Integer stock;

    public OutOfStockException(Long phoneId, Integer stock) {
        this.phoneId = phoneId;
        this.stock = stock;
    }

    public OutOfStockException(String message, Long phoneId, Integer stock) {
        super(message);
        this.phoneId = phoneId;
        this.stock = stock;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public Integer getStock(){
        return stock;
    }
}
