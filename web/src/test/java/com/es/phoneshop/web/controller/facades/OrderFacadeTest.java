package com.es.phoneshop.web.controller.facades;

import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.cart.CartService;
import com.es.core.cart.exceptions.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.StockDao;
import com.es.core.order.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class OrderFacadeTest {
    @Mock
    private StockDao stockDao;
    @Mock
    private OrderService orderService;
    @Mock
    private CartService cartService;
    @InjectMocks
    private OrderFacade orderFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    void setUpOrder(Order order, Phone phone1, Phone phone2, Phone phone3) {
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem1 = new OrderItem();
        OrderItem orderItem2 = new OrderItem();
        OrderItem orderItem3 = new OrderItem();

        orderItem1.setQuantity(100);
        orderItem2.setQuantity(2);
        orderItem3.setQuantity(3);

        orderItem1.setPhone(phone1);
        orderItem2.setPhone(phone2);
        orderItem3.setPhone(phone3);

        orderItems.add(orderItem1);
        orderItems.add(orderItem2);
        orderItems.add(orderItem3);

        order.setOrderItems(orderItems);
    }

    @Test
    void testPlaceOrderWithOutOfStock() throws OutOfStockException {
        Order order = new Order();
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();
        Phone phone3 = new Phone();
        Stock stock1 = new Stock();
        stock1.setStock(10);
        stock1.setReserved(10);
        Stock stock2 = new Stock();
        stock2.setStock(10);
        stock2.setReserved(5);
        Stock stock3 = new Stock();
        stock3.setStock(10);
        stock3.setReserved(0);

        setUpOrder(order, phone1, phone2, phone3);

        when(stockDao.getStock(phone1)).thenReturn(stock1);
        when(stockDao.getStock(phone2)).thenReturn(stock2);
        when(stockDao.getStock(phone3)).thenReturn(stock3);
        doNothing().when(cartService).update(anyList());

        assertThrows(OutOfStockException.class, () -> orderFacade.placeOrder(order));
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

        Stock stock1 = new Stock();
        stock1.setStock(1000);
        stock1.setReserved(10);
        Stock stock2 = new Stock();
        stock2.setStock(10);
        stock2.setReserved(5);
        Stock stock3 = new Stock();
        stock3.setStock(10);
        stock3.setReserved(0);

        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(phone1, 150));
        cartItems.add(new CartItem(phone2, 2));
        cartItems.add(new CartItem(phone3, 3));
        cart.setCartItems(cartItems);

        setUpOrder(order, phone1, phone2, phone3);

        when(stockDao.getStock(phone1)).thenReturn(stock1);
        when(stockDao.getStock(phone2)).thenReturn(stock2);
        when(stockDao.getStock(phone3)).thenReturn(stock3);
        when(cartService.getCart()).thenReturn(cart);
        doNothing().when(orderService).placeOrder(order);
        doNothing().when(cartService).remove(anyLong());

        orderFacade.placeOrder(order);

        assertEquals(110, stock1.getReserved());
        assertEquals(7, stock2.getReserved());
        assertEquals(3, stock3.getReserved());
        verify(orderService).placeOrder(order);
        assertEquals(50, cart.getCartItems().get(0).getQuantity());
        verify(cartService).remove(phone2.getId());
        verify(cartService).remove(phone3.getId());
    }
}
