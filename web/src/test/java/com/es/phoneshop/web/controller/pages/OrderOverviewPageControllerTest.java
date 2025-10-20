package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.model.order.Order;
import com.es.core.order.OrderService;
import com.es.phoneshop.web.constants.WebConstants;
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
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class OrderOverviewPageControllerTest {
    @Mock
    private CartService cartService;
    @Mock
    private OrderService orderService;
    @InjectMocks
    private OrderOverviewPageController orderOverviewPageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowOrderOverviewPage() {
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);
        Order order = new Order();
        Model model = new ExtendedModelMap();

        when(cartService.getCart()).thenReturn(cart);
        when(orderService.getOrderBySecureId("secureId")).thenReturn(Optional.of(order));

        String response = orderOverviewPageController.showOrderOverviewPage("secureId", model);

        verify(cartService, times(2)).getCart();
        assertEquals(order, model.getAttribute(WebConstants.ORDER_ATTR));
        assertEquals(BigDecimal.TEN, model.getAttribute(WebConstants.CART_COST_ATTR));
        assertEquals(1, model.getAttribute(WebConstants.CART_QUANTITY_ATTR));
        assertEquals("orderOverview", response);
    }
}
