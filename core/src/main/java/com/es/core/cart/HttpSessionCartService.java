package com.es.core.cart;

import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.model.constants.ExceptionConstants;
import com.es.core.model.exceptions.HighQuantityException;
import com.es.core.model.phone.JdbcPhoneDao;
import com.es.core.model.phone.Phone;
import com.es.core.cart.exceptions.OutOfStockException;
import com.es.core.stock.StockService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class HttpSessionCartService implements CartService {
    @Resource
    private Cart cart;
    @Resource
    private JdbcPhoneDao phoneDao;
    @Resource
    private StockService stockService;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public Cart getCart() {
        lock.readLock().lock();

        try {
            return cart;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void addPhone(Long phoneId, int quantity) throws ItemNotExistException, OutOfStockException {
        lock.writeLock().lock();

        try {
            CartItem cartItem = cart.getCartItems().stream()
                    .filter(item -> item.getPhone().getId().equals(phoneId))
                    .findFirst()
                    .orElse(null);

            Optional.ofNullable(cartItem)
                    .ifPresentOrElse(
                            cartItem1 -> updateCartItem(cartItem1, quantity),
                            () -> createCartItem(quantity, phoneId));

            recalculateCart(cart);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void update(List<CartItem> cartItems) throws OutOfStockException {
        lock.writeLock().lock();

        try {
            cartItems.forEach(this::checkStock);

            cart.setCartItems(cartItems);
            recalculateCart(cart);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void remove(Long phoneId) {
        lock.writeLock().lock();

        try {
            CartItem cartItem = cart.getCartItems().stream()
                    .filter(item -> item.getPhone().getId().equals(phoneId))
                    .findFirst()
                    .orElseThrow(() -> new ItemNotExistException(ExceptionConstants.PHONE_ID_NOT_FOUND_IN_CART));

            cart.getCartItems().remove(cartItem);
            recalculateCart(cart);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clearCart() {
        cart.getCartItems().clear();
        cart.setTotalCost(BigDecimal.ZERO);
        cart.setTotalQuantity(0);
    }

    private void checkStock(CartItem cartItem) throws OutOfStockException {
        if (!stockService.isPhoneInStock(cartItem.getPhone(), cartItem.getQuantity())) {
            throw new OutOfStockException(
                    cartItem.getPhone().getId(),
                    stockService.getStock(cartItem.getPhone()).getStock());
        }
    }

    private void updateCartItem(CartItem cartItem, int quantity) throws OutOfStockException {
        checkQuantity(quantity + cartItem.getQuantity());

        checkStock(cartItem);

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
    }

    private void createCartItem(int quantity, Long phoneId) throws OutOfStockException {
        Phone phone = phoneDao.get(phoneId)
                .orElseThrow(() -> new ItemNotExistException(ExceptionConstants.PHONE_ID_NOT_FOUND));
        checkQuantity(quantity);

        CartItem newCartItem = new CartItem(phone, quantity);

        checkStock(newCartItem);

        cart.getCartItems().add(newCartItem);
    }

    private void recalculateCart(Cart cart) {
        List<CartItem> cartItems = cart.getCartItems();
        BigDecimal newTotalCost = cartItems.stream()
                .map(this::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int newTotalQuantity = cartItems.stream().mapToInt(CartItem::getQuantity).sum();

        cart.setTotalCost(newTotalCost);
        cart.setTotalQuantity(newTotalQuantity);
    }

    private BigDecimal getTotalPrice(CartItem cartItem) {
        BigDecimal price = getPriceIfExistOrZero(cartItem);
        return price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }

    private BigDecimal getPriceIfExistOrZero(CartItem cartItem) {
        return Optional.ofNullable(cartItem.getPhone().getPrice()).orElse(BigDecimal.ZERO);
    }

    private void checkQuantity(int quantity) {
        if (quantity > ExceptionConstants.MAX_PHONES_IN_CART)
            throw new HighQuantityException(ExceptionConstants.QUANTITY_HIGHER_THAN_FIVE);
    }
}
