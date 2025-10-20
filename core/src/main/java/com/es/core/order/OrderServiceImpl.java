package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderDao;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
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
    public void placeOrder(Order order) {
        orderDao.saveOrderWithItems(order);
    }

    @Override
    public Optional<Order> getOrderBySecureId(String orderSecureId) {
        return orderDao.getBySecureId(orderSecureId);
    }

    private String generateOrderSecureId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
