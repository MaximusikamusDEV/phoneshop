package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.cart.exceptions.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;

import java.util.Optional;
import java.util.List;

public interface OrderService {
    Order createOrder(Cart cart);
    void placeOrder(Order order) throws OutOfStockException;
    Optional<Order> getOrderBySecureId(String orderSecureId);
    Optional<Order> getOrderById(Long orderId);
    Optional<List<Order>> getAllOrders();
    void updateOrderStatus(Order order, OrderStatus orderStatus);
}
