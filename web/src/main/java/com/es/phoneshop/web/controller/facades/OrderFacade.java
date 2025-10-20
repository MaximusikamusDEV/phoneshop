package com.es.phoneshop.web.controller.facades;

import com.es.core.cart.CartItem;
import com.es.core.cart.CartService;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import com.es.core.order.OrderService;
import com.es.core.cart.exceptions.OutOfStockException;
import com.es.phoneshop.web.constants.WebConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderFacade {
    @Resource
    private StockDao stockDao;
    @Resource
    private OrderService orderService;
    @Resource
    private CartService cartService;

    @Transactional
    public void placeOrder(Order order) throws OutOfStockException {
        List<OrderItem> availableItems = new ArrayList<>();
        List<OrderItem> oufOfStockItems = new ArrayList<>();

        order.getOrderItems().forEach(item -> checkItemStock(item, availableItems, oufOfStockItems));

        if (!oufOfStockItems.isEmpty()) {
            List<CartItem> newCartItems = new ArrayList<>();

            availableItems.forEach(item ->
                newCartItems.add(populateOrderItemToCartItem(item))
            );

            cartService.update(newCartItems);

            throw new OutOfStockException(WebConstants.OUT_OF_STOCK_INFO);
        }

        order.setOrderItems(availableItems);
        order.getOrderItems().forEach(this::setNewStock);
        orderService.placeOrder(order);
        updateCart(order);
    }

    private void updateCart(Order order) {
        order.getOrderItems().forEach(orderItem -> {
            cartService.getCart().getCartItems().stream()
                    .filter (item -> item.getPhone().getId().equals(orderItem.getPhone().getId()))
                    .findFirst()
                    .ifPresent(cartItem -> {
                        if(orderItem.getQuantity() < cartItem.getQuantity())
                            cartItem.setQuantity(cartItem.getQuantity() - orderItem.getQuantity());
                        else
                            cartService.remove(orderItem.getPhone().getId());
                    });
        });
    }

    private void setNewStock(OrderItem item) {
        Stock stock = stockDao.getStock(item.getPhone());
        stock.setReserved(stock.getReserved() + item.getQuantity());
        stockDao.setStock(stock);
    }

    private void checkItemStock(OrderItem item, List<OrderItem> availableItems, List<OrderItem> oufOfStockItems) {
        Stock stock = stockDao.getStock(item.getPhone());
        int availableStock = stock.getStock() - stock.getReserved();

        if (availableStock >= item.getQuantity())
            availableItems.add(item);
        else
            oufOfStockItems.add(item);
    }

    private CartItem populateOrderItemToCartItem(OrderItem item) {
        return new CartItem(item.getPhone(), item.getQuantity());
    }
}
