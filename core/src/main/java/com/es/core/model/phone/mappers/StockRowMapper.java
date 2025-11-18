package com.es.core.model.phone.mappers;

import com.es.core.model.phone.Stock;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StockRowMapper implements RowMapper<Stock> {
    @Override
    public Stock mapRow(ResultSet rs, int rowNum) throws SQLException {
        Stock stock = new Stock();
        stock.setStock(rs.getInt("stock"));
        stock.setReserved(rs.getInt("reserved"));
        return stock;
    }
}
