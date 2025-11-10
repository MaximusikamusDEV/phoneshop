package com.es.core.stock;

import com.es.core.cart.exceptions.OutOfStockException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;

public interface StockService {
    void reservePhone(Phone phone, int quantity);
    Stock getStock(Phone phone);
    boolean isPhoneInStock(Phone phone, int quantity) throws OutOfStockException;
    void confirmReserved(Phone phone, int quantity);
    void returnReservedToStock(Phone phone, int quantity);
}
