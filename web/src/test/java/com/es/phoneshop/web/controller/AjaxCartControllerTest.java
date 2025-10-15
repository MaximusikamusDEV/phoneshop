package com.es.phoneshop.web.controller;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.cart.exceptions.CartValidationException;
import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.order.OutOfStockException;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.dto.AjaxCartResponseDto;
import com.es.phoneshop.web.dto.CartItemDto;
import com.es.phoneshop.web.dto.MiniCartDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BindingResult;
import java.math.BigDecimal;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class AjaxCartControllerTest {
    @Mock
    private CartService cartService;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private AjaxCartController ajaxCartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPhoneSuccess() throws ItemNotExistException, OutOfStockException {
        CartItemDto dto = new CartItemDto();
        dto.setPhoneId(1L);
        dto.setQuantity(2);

        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<AjaxCartResponseDto> response = ajaxCartController.addPhone(dto, bindingResult);

        verify(cartService).addPhone(1L, 2);
        assertEquals(WebConstants.SUCCESS_PARAM, response.getBody().getStatus());
        assertEquals(WebConstants.SUCCESS_ADD_MESSAGE,response.getBody().getMessage());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testAddPhoneBindingResult() throws ItemNotExistException {
        CartItemDto dto = new CartItemDto();
        dto.setPhoneId(1L);
        dto.setQuantity(2);

        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(CartValidationException.class, () -> ajaxCartController.addPhone(dto, bindingResult));
    }

    @Test
    void testGetMiniCart() throws ItemNotExistException {
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);

        when(cartService.getCart()).thenReturn(cart);

        ResponseEntity<MiniCartDto> response = ajaxCartController.getMiniCart();

        assertEquals(0, response.getBody().getTotalCost().compareTo(BigDecimal.TEN));
        assertEquals(1, response.getBody().getTotalQuantity());
        assertEquals(200, response.getStatusCode().value());

        cart.setTotalCost(BigDecimal.valueOf(50));
        cart.setTotalQuantity(5);

        when(cartService.getCart()).thenReturn(cart);

        response = ajaxCartController.getMiniCart();

        assertEquals(0, response.getBody().getTotalCost().compareTo(BigDecimal.valueOf(50)));
        assertEquals(5, response.getBody().getTotalQuantity());
        assertEquals(200, response.getStatusCode().value());
    }
}
