package com.es.core.cart;

import com.es.core.cart.exceptions.ItemNotExistException;

import java.util.List;

public interface CartService {

    Cart getCart();

    void addPhone(Long phoneId, int quantity) throws ItemNotExistException;

    /**
     * @param items
     * key: {@link com.es.core.model.phone.Phone#id}
     * value: quantity
     */
    void update(List<CartItem> cartItems);

    void remove(Long phoneId) throws ItemNotExistException;
}
