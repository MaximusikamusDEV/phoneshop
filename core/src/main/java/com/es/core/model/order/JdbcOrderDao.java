package com.es.core.model.order;

import com.es.core.model.constants.DBConstants;
import com.es.core.model.constants.ExceptionConstants;
import com.es.core.model.exceptions.DatabaseUpdateException;
import com.es.core.model.order.resultSetExtractors.OrderSetExtractor;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class JdbcOrderDao implements OrderDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Resource
    private OrderItemsDao orderItemsDao;
    @Resource
    private OrderSetExtractor orderSetExtractor;

    @Override
    public Optional<Order> getBySecureId(String secureId) {
        List<Order> orders = jdbcTemplate.query(DBConstants.QUERY_GET_ORDER_BY_SECURE_ID, orderSetExtractor, secureId);

        return CollectionUtils.emptyIfNull(orders).stream().findFirst();
    }

    @Override
    public Optional<Order> getById(Long orderId) {
        List<Order> orders = jdbcTemplate.query(DBConstants.QUERY_GET_ORDER_BY_ID, orderSetExtractor, orderId);

        return CollectionUtils.emptyIfNull(orders).stream().findFirst();
    }

    @Override
    public void updateOrderStatus(Order order) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(order);
        int rowsUpdated = namedParameterJdbcTemplate.update(
                DBConstants.QUERY_UPDATE_ORDER_STATUS_BY_ID,
                parameters
        );

        if(rowsUpdated == 0) {
            throw new DatabaseUpdateException(ExceptionConstants.DATABASE_SAVE_PROBLEM);
        }
    }

    @Override
    @Transactional
    public void saveOrderWithItems(Order order) {
        if (order.getId() == null) {
            saveOrderWithNewId(order);
        } else {
            if (isExistingOrder(order)) {
                jdbcTemplate.update(DBConstants.QUERY_DELETE_ORDER_ITEMS, order.getId());
                orderItemsDao.saveOrderItems(order);
            } else {
                saveOrderWithNewId(order);
            }
        }
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query(DBConstants.QUERY_GET_ALL_ORDERS, orderSetExtractor);
    }

    private void saveOrderWithNewId(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(order);

        namedParameterJdbcTemplate.update(DBConstants.QUERY_SAVE_ORDER, parameters, keyHolder, new String[]{"id"});

        Optional<Number> id = Optional.ofNullable(keyHolder.getKey());

        id.ifPresentOrElse(number -> {
                    order.setId(number.longValue());
                    orderItemsDao.saveOrderItems(order);
                }, () -> {
                    throw new DatabaseUpdateException(ExceptionConstants.DATABASE_SAVE_PROBLEM);
                }
        );
    }

    private boolean isExistingOrder(Order order) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(order);
        int rowsUpdated = namedParameterJdbcTemplate.update(DBConstants.QUERY_UPDATE_ORDER_BY_ID, parameters);

        return rowsUpdated != 0;
    }
}
