package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.order.OrderService;
import com.es.core.stock.StockService;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.exceptions.EmptyOrderListException;
import com.es.phoneshop.web.exceptions.InvalidOrderIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class OrdersPageControllerTest {
    @Mock
    private OrderService orderService;
    @Mock
    private StockService stockService;
    @InjectMocks
    private OrdersPageController ordersPageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrderListPageSuccess() {
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        orders.add(order);
        Model model = new ExtendedModelMap();

        when(orderService.getAllOrders()).thenReturn(Optional.of(orders));

        String response = ordersPageController.getOrderListPage(model);

        assertEquals(orders, model.getAttribute(WebConstants.ORDER_LIST_ATTR));
        assertEquals("adminOrderList", response);
    }

    @Test
    void testGetOrderListPageEmptyOrderList() {
        List<Order> orders = null;
        Model model = new ExtendedModelMap();

        when(orderService.getAllOrders()).thenReturn(Optional.ofNullable(orders));

        assertThrows(EmptyOrderListException.class, () -> ordersPageController.getOrderListPage(model));
    }

    @Test
    void testGetOrderPageSuccess() {
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        orders.add(order);
        Model model = new ExtendedModelMap();
        Long orderId = 1L;

        when(orderService.getOrderById(orderId)).thenReturn(Optional.of(order));

        String response = ordersPageController.getOrderPage(orderId, model);

        assertEquals(order, model.getAttribute(WebConstants.ORDER_ATTR));
        assertEquals("adminOrderOverview", response);
    }

    @Test
    void testGetOrderPageIncorrectOrderId() {
        Model model = new ExtendedModelMap();
        Long orderId = 1345234L;

        assertThrows(InvalidOrderIdException.class, () -> ordersPageController.getOrderPage(orderId, model));
    }

    @Test
    void testChangeOrderStatusSuccess() {
        Order order = new Order();
        OrderItem orderItem = new OrderItem();
        OrderItem orderItem1 = new OrderItem();
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        orderItems.add(orderItem1);
        order.setOrderItems(orderItems);
        String status = OrderStatus.DELIVERED.toString();
        Model model = new ExtendedModelMap();
        Long orderId = 1L;

        when(orderService.getOrderById(orderId)).thenReturn(Optional.of(order));
        doNothing().when(stockService).confirmReserved(any(), anyInt());
        doNothing().when(orderService).updateOrderStatus(order, OrderStatus.DELIVERED);
        String response = ordersPageController.changeOrderStatus(orderId, status, model);

        assertEquals(order, model.getAttribute(WebConstants.ORDER_ATTR));
        assertEquals("adminOrderOverview", response);
        verify(stockService, times(2)).confirmReserved(any(), anyInt());

        status = OrderStatus.REJECTED.toString();
        doNothing().when(stockService).returnReservedToStock(any(), anyInt());

        response = ordersPageController.changeOrderStatus(orderId, status, model);

        assertEquals(order, model.getAttribute(WebConstants.ORDER_ATTR));
        assertEquals("adminOrderOverview", response);
        verify(stockService, times(2)).returnReservedToStock(any(), anyInt());
    }

    @Test
    void testChangeOrderStatusIncorrectOrderId() {
        String status = OrderStatus.DELIVERED.toString();
        Model model = new ExtendedModelMap();
        Long orderId = 3456L;

        assertThrows(InvalidOrderIdException.class,
                () -> ordersPageController.changeOrderStatus(orderId, status, model));
    }
}
