package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.cart.CartService;
import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.model.phone.Phone;
import com.es.core.order.OutOfStockException;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.controller.mappers.CartDtoMapper;
import com.es.phoneshop.web.dto.CartDto;
import com.es.phoneshop.web.dto.CartItemDto;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class CartPageControllerTest {
    @Mock
    private CartService cartService;
    @Mock
    private CartDtoMapper cartDtoMapper;
    @Mock
    BindingResult bindingResult;
    @InjectMocks
    private CartPageController cartPageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCartPage(){
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);
        Model model = new ExtendedModelMap();
        CartDto cartDto = new CartDto();

        when(cartService.getCart()).thenReturn(cart);
        when(cartDtoMapper.convertToCartDto(cart)).thenReturn(cartDto);

        String response = cartPageController.getCart(model);

        verify(cartService, times(2)).getCart();
        assertEquals(BigDecimal.TEN, model.getAttribute(WebConstants.CART_COST_ATTR));
        assertEquals(1, model.getAttribute(WebConstants.CART_QUANTITY_ATTR));
        assertEquals(cart, model.getAttribute(WebConstants.CART_ATTR));
        assertEquals(cartDto, model.getAttribute(WebConstants.CART_DTO_ATTR));
        assertEquals("cart", response);
    }


    @Test
    void testUpdateCartWithEmptyCart() throws OutOfStockException {
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);
        Model model = new ExtendedModelMap();
        CartDto cartDto = new CartDto();
        Long deletePhoneId = 1L;

        when(cartService.getCart()).thenReturn(cart);
        when(cartDtoMapper.convertToCartDto(cart)).thenReturn(cartDto);

        String response = cartPageController.updateCart(cartDto, bindingResult, model, deletePhoneId);

        assertEquals("redirect:/cart", response);
    }


    @Test
    void testUpdateCartWithDeletePhoneId() throws OutOfStockException {
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);

        Phone phone = new Phone();
        phone.setId(1L);

        CartItemDto cartItemDto = new CartItemDto(1L, 1);
        CartItem cartItem = new CartItem(phone, 1);

        Model model = new ExtendedModelMap();
        CartDto cartDto = new CartDto();
        List<CartItem> cartItems = new ArrayList<>();
        List<CartItemDto> cartDtoItems = new ArrayList<>();

        cartItems.add(cartItem);
        cartDtoItems.add(cartItemDto);

        cart.setCartItems(cartItems);
        cartDto.setItems(cartDtoItems);
        Long deletePhoneId = 1L;

        when(cartService.getCart()).thenReturn(cart);

        String response = cartPageController.updateCart(cartDto, bindingResult, model, deletePhoneId);

        assertEquals("redirect:/cart", response);
    }

    @Test
    void testUpdateCartWithDeletePhoneIdException() throws OutOfStockException {
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);

        Phone phone = new Phone();
        phone.setId(1L);

        CartItemDto cartItemDto = new CartItemDto(1L, 1);
        CartItem cartItem = new CartItem(phone, 1);

        Model model = new ExtendedModelMap();
        CartDto cartDto = new CartDto();
        List<CartItem> cartItems = new ArrayList<>();
        List<CartItemDto> cartDtoItems = new ArrayList<>();

        cartItems.add(cartItem);
        cartDtoItems.add(cartItemDto);

        cart.setCartItems(cartItems);
        cartDto.setItems(cartDtoItems);
        Long deletePhoneId = 1L;

        when(cartService.getCart()).thenReturn(cart);
        doThrow(ItemNotExistException.class).when(cartService).remove(deletePhoneId);

        String response = cartPageController.updateCart(cartDto, bindingResult, model, deletePhoneId);


        verify(cartService, times(2)).getCart();
        assertEquals(BigDecimal.TEN, model.getAttribute(WebConstants.CART_COST_ATTR));
        assertEquals(1, model.getAttribute(WebConstants.CART_QUANTITY_ATTR));
        assertEquals(cart, model.getAttribute(WebConstants.CART_ATTR));
        assertEquals(cartDto, model.getAttribute(WebConstants.CART_DTO_ATTR));
        assertEquals("cart", response);
    }


    @Test
    void testUpdateCartWithBindingResult() throws OutOfStockException {
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);
        Model model = new ExtendedModelMap();
        CartDto cartDto = new CartDto();
        Long deletePhoneId = 1L;

        when(cartService.getCart()).thenReturn(cart);
        when(cartDtoMapper.convertToCartDto(cart)).thenReturn(cartDto);
        when(bindingResult.hasErrors()).thenReturn(true);

        String response = cartPageController.updateCart(cartDto, bindingResult, model, deletePhoneId);

        verify(cartService).getCart();
        assertEquals(BigDecimal.TEN, model.getAttribute(WebConstants.CART_COST_ATTR));
        assertEquals(1, model.getAttribute(WebConstants.CART_QUANTITY_ATTR));
        assertEquals(cart, model.getAttribute(WebConstants.CART_ATTR));
        assertEquals(cartDto, model.getAttribute(WebConstants.CART_DTO_ATTR));
        assertEquals("cart", response);
    }

    @Test
    void testUpdateSuccess() throws OutOfStockException {
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);

        Phone phone = new Phone();
        phone.setId(1L);

        CartItemDto cartItemDto = new CartItemDto(1L, 1);
        CartItem cartItem = new CartItem(phone, 1);

        Model model = new ExtendedModelMap();
        CartDto cartDto = new CartDto();
        List<CartItem> cartItems = new ArrayList<>();
        List<CartItemDto> cartDtoItems = new ArrayList<>();

        cartItems.add(cartItem);
        cartDtoItems.add(cartItemDto);

        cart.setCartItems(cartItems);
        cartDto.setItems(cartDtoItems);
        Long deletePhoneId = null;

        when(cartService.getCart()).thenReturn(cart);
        when(cartDtoMapper.convertToCartDto(cart)).thenReturn(cartDto);
        when(cartDtoMapper.convertToCartItems(cartDto)).thenReturn(cartItems);
        when(bindingResult.hasErrors()).thenReturn(false);

        String response = cartPageController.updateCart(cartDto, bindingResult, model, deletePhoneId);

        verify(cartService).getCart();
        verify(cartDtoMapper).convertToCartItems(cartDto);
        verify(cartService).update(cartItems);
        assertEquals("redirect:/cart", response);
    }
}