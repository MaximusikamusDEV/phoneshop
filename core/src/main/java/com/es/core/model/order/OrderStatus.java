package com.es.core.model.order;

public enum OrderStatus {
    NEW("NEW"),
    DELIVERED("DELIVERED"),
    REJECTED("REJECTED");

    private final String code;

    OrderStatus(String code) {
        this.code = code;
    }

    public String getCode() {return code;}

    public static OrderStatus valueOfCode(String code) {
        for (OrderStatus orderStatus : values()) {
            if(orderStatus.name().equals(code.toUpperCase())){
                return orderStatus;
            }
        }
        return NEW;
    }
}
