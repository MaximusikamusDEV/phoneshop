package com.es.phoneshop.web.controller.pages;

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
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class OrderOverviewPageControllerTest {
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
        Order order = new Order();
        Model model = new ExtendedModelMap();

        when(orderService.getOrderBySecureId("secureId")).thenReturn(Optional.of(order));

        String response = orderOverviewPageController.showOrderOverviewPage("secureId", model);

        assertEquals(order, model.getAttribute(WebConstants.ORDER_ATTR));
        assertEquals("orderOverview", response);
    }
}
