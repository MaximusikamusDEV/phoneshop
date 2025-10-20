package com.es.phoneshop.web.dto;

import java.math.BigDecimal;

public class MiniCart {
    private int totalQuantity;
    private BigDecimal totalCost;

    public MiniCart(int totalQuantity, BigDecimal totalCost) {
        this.totalQuantity = totalQuantity;
        this.totalCost = totalCost;
    }

    public MiniCart() {
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
