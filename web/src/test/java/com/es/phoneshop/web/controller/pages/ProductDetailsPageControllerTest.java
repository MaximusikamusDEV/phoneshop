package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.model.phone.Phone;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.services.PhoneDisplayService;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class ProductDetailsPageControllerTest {
    @Mock
    private CartService cartService;
    @Mock
    private PhoneDisplayService phoneDisplayService;
    @InjectMocks
    private ProductDetailsPageController productDetailsPageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowProductDetails(){
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);
        Model model = new ExtendedModelMap();
        Phone phone = new Phone();

        when(cartService.getCart()).thenReturn(cart);
        when(phoneDisplayService.getPhoneById(1L)).thenReturn(phone);

        String response = productDetailsPageController.showProductDetails(1L, model);

        verify(phoneDisplayService).getPhoneById(1L);
        verify(cartService, times(2)).getCart();
        assertEquals(BigDecimal.TEN, model.getAttribute(WebConstants.CART_COST_ATTR));
        assertEquals(1, model.getAttribute(WebConstants.CART_QUANTITY_ATTR));
        assertEquals(phone, model.getAttribute(WebConstants.PHONE_ATTR));
        assertEquals("productDetails", response);
    }
}
