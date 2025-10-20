package com.es.core.model.order.resultSetExtractors;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.mappers.OrderMapper;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.mappers.ColorMapper;
import com.es.core.model.phone.mappers.PhoneMapper;
import jakarta.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.HashSet;

@Component
public class OrderSetExtractor implements ResultSetExtractor<List<Order>> {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private PhoneMapper phoneMapper;
    @Resource
    private ColorMapper colorMapper;

    @Override
    public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Order> orderMap = new LinkedHashMap<>();
        Map<Long, OrderItem> orderItemMap = new LinkedHashMap<>();

        while (rs.next()) {
            populateOrderMap(orderMap, rs);
            populateOrderItemMap(orderItemMap, rs, orderMap);
        }

        return new ArrayList<>(orderMap.values());
    }

    private void populateOrderMap(Map<Long, Order> orderMap, ResultSet rs) throws SQLException {
        Long orderId = rs.getLong("id");
        Order order = orderMap.get(orderId);

        if(order == null){
            order = orderMapper.mapRow(rs);
            order.setOrderItems(new ArrayList<>());
            orderMap.put(orderId, order);
        }
    }

    private void populateOrderItemMap(Map<Long, OrderItem> orderItemMap,
                                      ResultSet rs,
                                      Map<Long, Order> orderMap) throws SQLException {
        Long orderItemId = rs.getLong("itemId");
        OrderItem orderItem = orderItemMap.get(orderItemId);

        Long orderId = rs.getLong("id");
        Order order = orderMap.get(orderId);

        if(orderItem == null){
            orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setId(rs.getLong("itemId"));
            orderItem.setQuantity(rs.getInt("quantity"));

            Phone phone = phoneMapper.mapRow(rs);
            phone.setId(rs.getLong("phoneId"));
            phone.setColors(new HashSet<>());
            orderItem.setPhone(phone);

            order.getOrderItems().add(orderItem);

            orderItemMap.put(orderItemId, orderItem);
        }

        Long colorId  = (Long) rs.getObject("color_id");

        if(colorId != null){
            Color color = colorMapper.mapRow(rs);
            orderItem.getPhone().getColors().add(color);
        }
    }
}
