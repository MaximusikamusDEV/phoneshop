package com.es.core.stock;

import com.es.core.cart.exceptions.OutOfStockException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;

public interface StockService {
    void reservePhone(Phone phone, int quantity);
    void setStock(Stock stock);
    Stock getStock(Phone phone);
    boolean isPhoneInStock(Phone phone, int quantity) throws OutOfStockException;
}
