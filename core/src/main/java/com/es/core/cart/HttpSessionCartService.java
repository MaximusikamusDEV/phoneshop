package com.es.core.cart;

import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.model.constants.ExceptionConstants;
import com.es.core.model.phone.JdbcPhoneDao;
import com.es.core.model.phone.JdbcStockDao;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.order.OutOfStockException;
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
    @Resource
    JdbcStockDao stockDao;

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void addPhone(Long phoneId, int quantity) throws ItemNotExistException, OutOfStockException {
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getPhone().getId().equals(phoneId))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            checkStock(cartItem.getPhone(), quantity);
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            Phone phone = phoneDao.get(phoneId)
                    .orElseThrow(() -> new ItemNotExistException(ExceptionConstants.PHONE_ID_NOT_FOUND));
            checkStock(phone, quantity);
            CartItem newCartItem = new CartItem(phone, quantity);
            cart.getCartItems().add(newCartItem);
        }

        recalculateCart(cart);
    }

    @Override
    public void update(List<CartItem> cartItems) throws OutOfStockException {
        for (CartItem cartItem : cartItems) {
            checkStock(cartItem.getPhone(), cartItem.getQuantity());
        }

        cart.setCartItems(cartItems);
        recalculateCart(cart);
    }

    @Override
    public void remove(Long phoneId) {
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getPhone().getId().equals(phoneId))
                .findFirst()
                .orElseThrow(() -> new ItemNotExistException(ExceptionConstants.PHONE_ID_NOT_FOUND_IN_CART));

        cart.getCartItems().remove(cartItem);
        recalculateCart(cart);
    }

    private void recalculateCart(Cart cart) {
        List<CartItem> cartItems = cart.getCartItems();
        BigDecimal newTotalCost = cartItems.stream()
                .map(item -> {
                    BigDecimal price = getPriceIfExistOrZero(item);
                    return price.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int newTotalQuantity = cartItems.stream().mapToInt(CartItem::getQuantity).sum();

        cart.setTotalCost(newTotalCost);
        cart.setTotalQuantity(newTotalQuantity);
    }

    private BigDecimal getPriceIfExistOrZero(CartItem cartItem) {
        return Optional.ofNullable(cartItem.getPhone().getPrice()).orElse(BigDecimal.ZERO);
    }

    private void checkStock(Phone phone, int quantity) throws OutOfStockException {
        Stock stock = stockDao.getStock(phone);
        if (quantity > (stock.getStock() - stock.getReserved()))
            throw new OutOfStockException(phone.getId());
    }
}
