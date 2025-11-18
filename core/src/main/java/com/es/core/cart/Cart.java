package com.es.core.cart;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@SessionScope
public class Cart {
    private List<CartItem> cartItems;
    private int totalQuantity;
    private BigDecimal totalCost;

    public Cart() {
        cartItems = new ArrayList<>();
        totalQuantity = 0;
        totalCost = BigDecimal.ZERO;
    }

    public Cart(List<CartItem> cartItems, int totalQuantity, BigDecimal totalCost) {
        this.cartItems = cartItems;
        this.totalQuantity = totalQuantity;
        this.totalCost = totalCost;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
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
