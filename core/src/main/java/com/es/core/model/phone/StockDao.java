package com.es.core.model.phone;

public interface StockDao {
    public Stock getStock(Phone phone);
    public void setStock(Stock stock);
}
