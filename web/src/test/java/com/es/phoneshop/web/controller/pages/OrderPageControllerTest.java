package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.cart.CartService;
import com.es.core.cart.exceptions.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.Phone;
import com.es.core.order.OrderService;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.controller.forms.OrderForm;
import com.es.phoneshop.web.exceptions.EmptyCartException;
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
import org.springframework.validation.BindingResult;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class OrderPageControllerTest {
    @Mock
    private CartService cartService;
    @Mock
    private OrderService orderService;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private OrderPageController orderPageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrderSuccess(){
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(new Phone(), 1));
        cart.setCartItems(cartItems);
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);
        Model model = new ExtendedModelMap();
        Order order = new Order();

        when(cartService.getCart()).thenReturn(cart);
        when(orderService.createOrder(cart)).thenReturn(order);

        String response = orderPageController.getOrder(model);

        verify(cartService).getCart();
        assertEquals(order, model.getAttribute(WebConstants.ORDER_ATTR));
        assertEquals("order", response);
    }

    @Test
    void testGetOrderException(){
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);
        Model model = new ExtendedModelMap();

        when(cartService.getCart()).thenReturn(cart);

        assertThrows(EmptyCartException.class, () -> orderPageController.getOrder(model));

        List<CartItem> cartItems = new ArrayList<>();
        cart.setCartItems(cartItems);

        assertThrows(EmptyCartException.class, () -> orderPageController.getOrder(model));
    }

    @Test
    void testPlaceOrderNullOrder(){
        Model model = new ExtendedModelMap();
        Order order = new Order();
        Cart cart = new Cart();
        OrderForm orderForm = new OrderForm();

        when(cartService.getCart()).thenReturn(cart);
        when(orderService.createOrder(cart)).thenReturn(order);

        String response = orderPageController.placeOrder(orderForm, bindingResult, model);

        assertEquals("redirect:/order", response);
    }

    @Test
    void testPlaceOrderNullOrderForm(){
        Model model = new ExtendedModelMap();
        OrderForm orderForm = null;

        String response = orderPageController.placeOrder(orderForm, bindingResult, model);

        assertEquals("redirect:/order", response);
    }

    @Test
    void testPlaceOrderWithErrors(){
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(new Phone(), 1));
        cart.setCartItems(cartItems);
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);
        Model model = new ExtendedModelMap();
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);
        OrderForm orderForm = new OrderForm();
        orderForm.setLastName("John");
        orderForm.setFirstName("Jane");
        orderForm.setAdditionalInfo("info");
        orderForm.setDeliveryAddress("address");
        orderForm.setContactPhoneNo("phoneNo");
        order.setSecureId("secureId");

        when(orderService.createOrder(cart)).thenReturn(order);
        when(cartService.getCart()).thenReturn(cart);
        when(bindingResult.hasErrors()).thenReturn(true);

        String response = orderPageController.placeOrder(orderForm, bindingResult, model);

        verify(cartService).getCart();
        assertEquals(order, model.getAttribute(WebConstants.ORDER_ATTR));
        assertEquals("order", response);
    }

    @Test
    void testPlaceOrderWithEmptyOrderItems(){
        Model model = new ExtendedModelMap();
        OrderForm orderForm = new OrderForm();
        Order order = new Order();
        Cart cart = new Cart();

        when(cartService.getCart()).thenReturn(cart);
        when(orderService.createOrder(cart)).thenReturn(order);
        when(bindingResult.hasErrors()).thenReturn(false);

        String response = orderPageController.placeOrder(orderForm, bindingResult, model);

        assertEquals("redirect:/order", response);
    }

    @Test
    void testPlaceOrderWithOutOfStockItems() throws OutOfStockException {
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(new Phone(), 1));
        cart.setCartItems(cartItems);
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);
        Model model = new ExtendedModelMap();
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);
        OrderForm orderForm = new OrderForm();
        orderForm.setLastName("John");
        orderForm.setFirstName("Jane");
        orderForm.setAdditionalInfo("info");
        orderForm.setDeliveryAddress("address");
        orderForm.setContactPhoneNo("phoneNo");

        when(orderService.createOrder(cart)).thenReturn(order);
        when(cartService.getCart()).thenReturn(cart);
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(OutOfStockException.class).when(orderService).placeOrder(order);
        when(orderService.createOrder(cart)).thenReturn(order);

        String response = orderPageController.placeOrder(orderForm, bindingResult, model);

        verify(cartService, times(2)).getCart();
        assertEquals(order.getFirstName(), orderForm.getFirstName());
        assertEquals(order.getLastName(), orderForm.getLastName());
        assertEquals(order.getAdditionalInfo(), orderForm.getAdditionalInfo());
        assertEquals(order.getDeliveryAddress(), orderForm.getDeliveryAddress());
        assertEquals(order.getContactPhoneNo(), orderForm.getContactPhoneNo());
        assertEquals(order, model.getAttribute(WebConstants.ORDER_ATTR));
        assertEquals("order", response);
    }

    @Test
    void testPlaceOrderSuccess() throws OutOfStockException {
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(new Phone(), 1));
        cart.setCartItems(cartItems);
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);
        Model model = new ExtendedModelMap();
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);
        OrderForm orderForm = new OrderForm();
        orderForm.setLastName("John");
        orderForm.setFirstName("Jane");
        orderForm.setAdditionalInfo("info");
        orderForm.setDeliveryAddress("address");
        orderForm.setContactPhoneNo("phoneNo");
        order.setSecureId("secureId");

        when(cartService.getCart()).thenReturn(cart);
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(orderService).placeOrder(order);
        when(orderService.createOrder(cart)).thenReturn(order);


        String response = orderPageController.placeOrder(orderForm, bindingResult, model);

        assertEquals(order.getFirstName(), orderForm.getFirstName());
        assertEquals(order.getLastName(), orderForm.getLastName());
        assertEquals(order.getAdditionalInfo(), orderForm.getAdditionalInfo());
        assertEquals(order.getDeliveryAddress(), orderForm.getDeliveryAddress());
        assertEquals(order.getContactPhoneNo(), orderForm.getContactPhoneNo());
        assertEquals("redirect:/orderOverview/secureId", response);
    }
}