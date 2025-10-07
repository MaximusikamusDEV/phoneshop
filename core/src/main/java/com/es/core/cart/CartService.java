package com.es.core.cart;

import com.es.core.cart.Exceptions.ItemNotExsistException;
import java.math.BigDecimal;
import java.util.Map;

public interface CartService {

    Cart getCart();

    void addPhone(Long phoneId, Long quantity);

    /**
     * @param items
     * key: {@link com.es.core.model.phone.Phone#id}
     * value: quantity
     */
    void update(Map<Long, Long> items);

    void remove(Long phoneId) throws ItemNotExsistException;

    int getCartQuantity();

    BigDecimal getTotalCost();
}
