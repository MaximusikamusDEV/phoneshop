package com.es.phoneshop.web.dto;

import java.math.BigDecimal;

public class AjaxCartResponseDto {
    private String status;
    private String message;
    private int totalQuantity;
    private BigDecimal totalCost;

    public AjaxCartResponseDto() {
    }

    public AjaxCartResponseDto(String status, String message, int totalQuantity, BigDecimal totalCost) {
        this.status = status;
        this.message = message;
        this.totalQuantity = totalQuantity;
        this.totalCost = totalCost;
    }

    public AjaxCartResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
        this.totalQuantity = 0;
        this.totalCost = BigDecimal.ZERO;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
