package com.es.core.model.order.mappers;

import com.es.core.model.order.Order;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OrderMapper{
    public Order mapRow(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setSecureId(rs.getString("secureId"));
        order.setSubtotal(rs.getBigDecimal("subtotal"));
        order.setDeliveryPrice(rs.getBigDecimal("deliveryPrice"));
        order.setTotalPrice(rs.getBigDecimal("totalPrice"));
        order.setFirstName(rs.getString("firstName"));
        order.setLastName(rs.getString("lastName"));
        order.setDeliveryAddress(rs.getString("deliveryAddress"));
        order.setContactPhoneNo(rs.getString("contactPhoneNo"));
        order.setAdditionalInfo(rs.getString("additionalInfo"));
        order.setCreatedAtAsTimestamp(rs.getTimestamp("createdAt"));
        order.setStatusAsString(rs.getString("status"));

        return order;
    }
}
