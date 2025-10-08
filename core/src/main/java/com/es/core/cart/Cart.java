package com.es.core.cart;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class Cart {
    private Map<Long, Long> cart = new HashMap<>();

    public Map<Long, Long> getCart() {
        return cart;
    }

    public void setCart(Map<Long, Long> cart) {
        this.cart = cart;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Cart cart1 = (Cart) object;
        return Objects.equals(cart, cart1.cart);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cart);
    }
}
