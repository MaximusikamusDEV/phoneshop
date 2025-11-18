package com.es.core.model.order;

import com.es.core.model.constants.DBConstants;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcOrderItemsDao implements OrderItemsDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveOrderItems(Order order) {
        if (order.getOrderItems() != null) {
            order.getOrderItems().forEach(orderItem ->
                    jdbcTemplate.update(
                            DBConstants.QUERY_SAVE_ORDER_ITEM,
                            orderItem.getOrder().getId(),
                            orderItem.getPhone().getId(),
                            orderItem.getQuantity())
            );
        }
    }
}
