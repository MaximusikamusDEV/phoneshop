package com.es.core.cart;

import com.es.core.cart.Exceptions.ItemNotExsistException;
import com.es.core.model.phone.JdbcPhoneDao;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HttpSessionCartService implements CartService {
    @Resource
    private Cart cart;
    @Resource
    JdbcPhoneDao phoneDao;

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void addPhone(Long phoneId, Long quantity) {
        if (cart.getCart().containsKey(phoneId)) {
            cart.getCart().put(phoneId, cart.getCart().get(phoneId) + quantity);
        } else
            cart.getCart().put(phoneId, quantity);
    }

    @Override
    public void update(Map<Long, Long> items) {
        cart.setCart(items);
    }

    @Override
    public void remove(Long phoneId) throws ItemNotExsistException {
        if (cart.getCart().containsKey(phoneId)) {
            cart.getCart().remove(phoneId);
        } else {
            throw new ItemNotExsistException("Item not found");
        }
    }

    @Override
    public int getCartQuantity() {
        if (cart.getCart().isEmpty()) {
            return 0;
        } else {
            List<Long> quantities = new ArrayList<>(cart.getCart().values());
            return quantities.stream().mapToInt(Long::intValue).sum();
        }
    }

    @Override
    public BigDecimal getTotalCost() {
        return cart.getCart().entrySet().stream()
                .map(entry -> {
                    Long phoneId = entry.getKey();
                    Long quantity = entry.getValue();

                    return phoneDao.get(phoneId)
                            .flatMap(phone -> Optional.ofNullable(phone.getPrice())
                                    .map(price -> price.multiply(BigDecimal.valueOf(quantity))))
                            .orElse(BigDecimal.ZERO);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
