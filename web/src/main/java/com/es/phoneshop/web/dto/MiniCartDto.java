package com.es.phoneshop.web.dto;

import java.math.BigDecimal;

public class MiniCartDto {
    int totalQuantity;
    BigDecimal totalCost;

    public MiniCartDto(int totalQuantity, BigDecimal totalCost) {
        this.totalQuantity = totalQuantity;
        this.totalCost = totalCost;
    }

    public MiniCartDto() {
        this.totalQuantity = 0;
        this.totalCost = BigDecimal.ZERO;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
