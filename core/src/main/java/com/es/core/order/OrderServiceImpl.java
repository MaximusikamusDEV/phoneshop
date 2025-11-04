package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.cart.CartService;
import com.es.core.cart.exceptions.OutOfStockException;
import com.es.core.model.constants.ExceptionConstants;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderDao;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.stock.StockService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private StockService stockService;
    @Resource
    private CartService cartService;
    @Resource
    private OrderDao orderDao;
    @Value("${delivery.price}")
    private BigDecimal deliveryPrice;

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();

        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setPhone(cartItem.getPhone());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setOrder(order);
                    return orderItem;
                }).toList();

        order.setOrderItems(orderItems);
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryPrice(deliveryPrice);
        order.setTotalPrice(cart.getTotalCost().add(deliveryPrice));
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setSecureId(generateOrderSecureId());

        return order;
    }

    @Override
    @Transactional
    public void placeOrder(Order order) throws OutOfStockException {
        List<OrderItem> availableItems = new ArrayList<>();
        List<OrderItem> oufOfStockItems = new ArrayList<>();

        order.getOrderItems().forEach(item -> {
            if (isPhoneInStock(item.getPhone(), item.getQuantity())) {
                availableItems.add(item);
            } else {
                oufOfStockItems.add(item);
            }
        });

        if (!oufOfStockItems.isEmpty()) {
            List<CartItem> newCartItems = new ArrayList<>();

            availableItems.forEach(item ->
                    newCartItems.add(populateOrderItemToCartItem(item))
            );

            cartService.update(newCartItems);

            throw new OutOfStockException(ExceptionConstants.OUT_OF_STOCK_INFO);
        }

        order.setOrderItems(availableItems);

        order.getOrderItems().forEach(item ->
                stockService.reservePhone(item.getPhone(), item.getQuantity())
        );

        orderDao.saveOrderWithItems(order);
        cartService.clearCart();
    }

    @Override
    public Optional<Order> getOrderBySecureId(String orderSecureId) {
        return orderDao.getBySecureId(orderSecureId);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Optional<Order> getOrderById(Long orderId) {
        return orderDao.getById(orderId);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Optional<List<Order>> getAllOrders() {
        return Optional.ofNullable(orderDao.findAll());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void updateOrderStatus(Order order, OrderStatus orderStatus) {
        if (orderStatus.equals(OrderStatus.DELIVERED)) {
            order.getOrderItems().forEach(orderItem ->
                    stockService.confirmReserved(orderItem.getPhone(), orderItem.getQuantity())
            );
        } else {
            order.getOrderItems().forEach(orderItem ->
                    stockService.returnReservedToStock(orderItem.getPhone(), orderItem.getQuantity())
            );
        }

        order.setStatus(orderStatus);
        orderDao.updateOrderStatus(order);
    }

    private String generateOrderSecureId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private CartItem populateOrderItemToCartItem(OrderItem item) {
        return new CartItem(item.getPhone(), item.getQuantity());
    }

    private boolean isPhoneInStock(Phone phone, int quantity) throws OutOfStockException {
        Stock stock = stockService.getStock(phone);
        return quantity <= stock.getStock();
    }
}
