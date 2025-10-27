package com.es.core.model.phone;

public interface StockDao {
    Stock getStock(Phone phone);
    void setStock(Stock stock);
    void reservePhone(Phone phone, int quantity);
}
