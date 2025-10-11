package com.es.core.cart;

import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.model.phone.JdbcPhoneDao;
import com.es.core.model.phone.Phone;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
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
    public void addPhone(Long phoneId, int quantity) throws ItemNotExistException {
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getPhone().getId().equals(phoneId))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            BigDecimal price = getPriceIfExistOrZero(cartItem);
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cart.setTotalCost(cart.getTotalCost().add(price.multiply(BigDecimal.valueOf(quantity))));
            cart.setTotalQuantity(cart.getTotalQuantity() + quantity);
        } else {
            Phone phone = phoneDao.get(phoneId)
                    .orElseThrow(() -> new ItemNotExistException("PhoneId doesn't exist"));

            CartItem newCartItem = new CartItem(phone, quantity);
            BigDecimal price = getPriceIfExistOrZero(newCartItem);
            cart.getCartItems().add(newCartItem);
            cart.setTotalCost(cart.getTotalCost().add(price.multiply(BigDecimal.valueOf(quantity))));
            cart.setTotalQuantity(cart.getTotalQuantity() + quantity);
        }
    }

    @Override
    public void update(List<CartItem> cartItems) {
        BigDecimal newTotalPrice = cartItems.stream()
                .map(item -> {
                    BigDecimal price = getPriceIfExistOrZero(item);
                    return price.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int newTotalQuantity = cartItems.stream().mapToInt(CartItem::getQuantity).sum();

        cart.setCartItems(cartItems);
        cart.setTotalCost(newTotalPrice);
        cart.setTotalQuantity(newTotalQuantity);
    }

    @Override
    public void remove(Long phoneId) throws ItemNotExistException {
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getPhone().getId().equals(phoneId))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            BigDecimal price = getPriceIfExistOrZero(cartItem);
            cart.setTotalQuantity(cart.getTotalQuantity() - cartItem.getQuantity());
            cart.setTotalCost(
                    cart.getTotalCost().subtract(price.multiply(BigDecimal.valueOf(cartItem.getQuantity()))));
            cart.getCartItems().remove(cartItem);
        } else
            throw new ItemNotExistException("PhoneId doesn't exist in cart");
    }

    private BigDecimal getPriceIfExistOrZero(CartItem cartItem) {
        return Optional.ofNullable(cartItem.getPhone().getPrice()).orElse(BigDecimal.ZERO);
    }
}
