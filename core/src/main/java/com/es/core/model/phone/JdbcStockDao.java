package com.es.core.model.phone;

import com.es.core.model.constants.DBConstants;
import com.es.core.model.constants.ExceptionConstants;
import com.es.core.model.exceptions.DatabaseException;
import com.es.core.model.phone.mappers.StockRowMapper;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class JdbcStockDao implements StockDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Stock getStock(Phone phone) {
        Optional<Stock> stock = Optional.ofNullable(
                jdbcTemplate.queryForObject(DBConstants.QUERY_GET_STOCK, new StockRowMapper(), phone.getId()));

        stock.ifPresent(s -> s.setPhone(phone));
        return stock.orElseThrow(() -> new DatabaseException(ExceptionConstants.DATABASE_PROBLEM));
    }

    @Override
    public void setStock(Stock stock) {
        int rowsUpdated = jdbcTemplate.update(
                DBConstants.QUERY_UPDATE_STOCK,
                stock.getStock(),
                stock.getReserved(),
                stock.getPhone().getId());

        if (rowsUpdated == 0) {
            jdbcTemplate.update(
                    DBConstants.QUERY_INSERT_STOCK,
                    stock.getPhone().getId(),
                    stock.getStock(),
                    stock.getReserved());
        }
    }
}
