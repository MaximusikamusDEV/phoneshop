package com.es.core.model.order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    void saveOrderWithItems(Order order);
    List<Order> findAll();
    Optional<Order> getBySecureId(String secureId);
    Optional<Order> getById(Long orderId);
    void updateOrderStatus(Order order);
}
