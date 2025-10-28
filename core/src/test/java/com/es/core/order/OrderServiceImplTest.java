package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.cart.CartService;
import com.es.core.cart.exceptions.OutOfStockException;
import com.es.core.model.order.OrderDao;
import com.es.core.model.phone.*;
import com.es.core.stock.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.math.BigDecimal;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import java.util.Set;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
public class OrderServiceImplTest {
    @Mock
    private PhoneDao phoneDao;
    @Mock
    private CartService cartService;
    @Mock
    private OrderDao orderDao;
    @Mock
    private StockService stockService;
    @InjectMocks
    private OrderServiceImpl orderService;
    private Phone createdPhone;

    @BeforeEach()
    void setUpData() {
        MockitoAnnotations.openMocks(this);

        createdPhone = new Phone();
        Color createdColor = new Color();

        createdColor.setId(1013L);
        createdColor.setCode("TEST");

        createdPhone.setId(1L);
        createdPhone.setBrand("ARCHOSTEST");
        createdPhone.setModel("ARCHOS 101 G9");
        createdPhone.setPrice(null);
        createdPhone.setColors(Set.of(createdColor));

        when(phoneDao.get(anyLong())).thenReturn(Optional.of(createdPhone));

        try {
            var field = OrderServiceImpl.class.getDeclaredField("deliveryPrice");
            field.setAccessible(true);
            field.set(orderService, BigDecimal.valueOf(5));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreateOrder() {
        Cart cart = new Cart();
        cart.setCartItems(List.of(new CartItem(createdPhone, 1)));
        cart.setTotalCost(BigDecimal.valueOf(10));

        Order order = orderService.createOrder(cart);

        assertNotNull(order);
        assertEquals(1, order.getOrderItems().size());
        assertEquals(cart.getTotalCost(), order.getSubtotal());
        assertEquals(OrderStatus.NEW, order.getStatus());
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getSecureId());
    }

    @Test
    void testGetOrderBySecureId() throws OutOfStockException {
        Order order = new Order();

        when(orderDao.getBySecureId(anyString())).thenReturn(Optional.of(order));
        Order getOrder = orderService.getOrderBySecureId("Id").get();

        assertNotNull(getOrder);
        assertEquals(order.getId(), getOrder.getId());
        verify(orderDao, times(1)).getBySecureId(anyString());
    }

    @Test
    void testGetOrderById() throws OutOfStockException {
        Order order = new Order();

        when(orderDao.getById(anyLong())).thenReturn(Optional.of(order));
        Order getOrder = orderService.getOrderById(1L).get();

        assertNotNull(getOrder);
        assertEquals(order.getId(), getOrder.getId());
        verify(orderDao, times(1)).getById(anyLong());
    }

    @Test
    void testGetAllOrders() throws OutOfStockException {
        Order order = new Order();
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        when(orderDao.findAll()).thenReturn(orders);
        List<Order> getOrders = orderService.getAllOrders().get();

        assertNotNull(getOrders);
        assertEquals(orders, getOrders);
        verify(orderDao, times(1)).findAll();
    }

    @Test
    void testUpdateOrderStatus() throws OutOfStockException {
        Order order = new Order();

        doNothing().when(orderDao).updateOrderStatus(order);
        orderService.updateOrderStatus(order, OrderStatus.DELIVERED);

        assertNotNull(order);
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        verify(orderDao, times(1)).updateOrderStatus(order);
    }

    @Test
    void testPlaceOrderWithOutOfStock() throws OutOfStockException {
        Order order = new Order();
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();
        Phone phone3 = new Phone();
        List<OrderItem> items = new ArrayList<>();
        OrderItem item1 = new OrderItem();
        item1.setId(1L);
        item1.setPhone(phone1);
        item1.setQuantity(1);
        item1.setOrder(order);

        OrderItem item2 = new OrderItem();
        item2.setId(2L);
        item2.setPhone(phone2);
        item2.setQuantity(5);
        item2.setOrder(order);

        OrderItem item3 = new OrderItem();
        item3.setId(3L);
        item3.setPhone(phone3);
        item3.setQuantity(3);
        item3.setOrder(order);

        items.add(item1);
        items.add(item2);
        items.add(item3);

        order.setOrderItems(items);

        Stock stock = new Stock();
        stock.setReserved(10);
        stock.setStock(1);

        doNothing().when(cartService).update(anyList());
        when(stockService.getStock(phone1)).thenReturn(stock);
        when(stockService.getStock(phone2)).thenReturn(stock);
        when(stockService.getStock(phone3)).thenReturn(stock);

        assertThrows(OutOfStockException.class, () -> orderService.placeOrder(order));

        verify(cartService).update(anyList());
        verify(stockService, never()).reservePhone(any(), anyInt());
        verify(orderDao, never()).saveOrderWithItems(any());
    }

    @Test
    void testPlaceOrderSuccess() throws OutOfStockException {
        Order order = new Order();
        Phone phone1 = new Phone();
        phone1.setId(1L);
        Phone phone2 = new Phone();
        phone2.setId(2L);
        Phone phone3 = new Phone();
        phone3.setId(3L);

        List<OrderItem> items = new ArrayList<>();
        OrderItem item1 = new OrderItem();
        item1.setId(1L);
        item1.setPhone(phone1);
        item1.setQuantity(1);
        item1.setOrder(order);

        OrderItem item2 = new OrderItem();
        item2.setId(2L);
        item2.setPhone(phone2);
        item2.setQuantity(5);
        item2.setOrder(order);

        OrderItem item3 = new OrderItem();
        item3.setId(3L);
        item3.setPhone(phone3);
        item3.setQuantity(3);
        item3.setOrder(order);

        items.add(item1);
        items.add(item2);
        items.add(item3);

        order.setOrderItems(items);

        Stock stock = new Stock();
        stock.setReserved(10);
        stock.setStock(1000);

        when(stockService.getStock(phone1)).thenReturn(stock);
        when(stockService.getStock(phone2)).thenReturn(stock);
        when(stockService.getStock(phone3)).thenReturn(stock);
        doNothing().when(cartService).clearCart();
        doNothing().when(orderDao).saveOrderWithItems(order);
        doNothing().when(stockService).reservePhone(any(), anyInt());

        orderService.placeOrder(order);

        verify(stockService, times(3)).reservePhone(any(), anyInt());
        verify(orderDao).saveOrderWithItems(order);
        verify(cartService).clearCart();
    }
}
