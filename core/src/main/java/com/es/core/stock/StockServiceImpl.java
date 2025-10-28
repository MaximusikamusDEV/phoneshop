package com.es.core.stock;

import com.es.core.cart.exceptions.OutOfStockException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl implements StockService {
    @Resource
    private StockDao stockDao;

    @Override
    public void reservePhone(Phone phone, int quantity) {
        Stock stock = stockDao.getStock(phone);
        stock.setReserved(stock.getReserved() + quantity);
        stock.setStock(stock.getStock() - quantity);

        stockDao.setStock(stock);
    }

    @Override
    public void setStock(Stock stock) {
        stockDao.setStock(stock);
    }

    @Override
    public Stock getStock(Phone phone){
        return stockDao.getStock(phone);
    }

    @Override
    public boolean isPhoneInStock(Phone phone, int quantity) throws OutOfStockException {
        Stock stock = stockDao.getStock(phone);
        return quantity <= stock.getStock();
    }
}
