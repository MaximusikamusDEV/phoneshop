package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.cart.CartService;
import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.model.phone.Phone;
import com.es.core.cart.exceptions.OutOfStockException;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.controller.mappers.CartFormMapper;
import com.es.phoneshop.web.controller.forms.CartForm;
import com.es.phoneshop.web.controller.forms.CartItemForm;
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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class CartPageControllerTest {
    @Mock
    private CartService cartService;
    @Mock
    private CartFormMapper cartFormMapper;
    @Mock
    private BindingResult bindingResult;
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
        CartForm cartForm = new CartForm();

        when(cartService.getCart()).thenReturn(cart);
        when(cartFormMapper.convertToCartForm(cart)).thenReturn(cartForm);

        String response = cartPageController.getCart(model);

        verify(cartService, times(2)).getCart();
        assertEquals(BigDecimal.TEN, model.getAttribute(WebConstants.CART_COST_ATTR));
        assertEquals(1, model.getAttribute(WebConstants.CART_QUANTITY_ATTR));
        assertEquals(cart, model.getAttribute(WebConstants.CART_ATTR));
        assertEquals(cartForm, model.getAttribute(WebConstants.CART_FORM_ATTR));
        assertEquals("cart", response);
    }


    @Test
    void testUpdateCartWithEmptyCart(){
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);
        Model model = new ExtendedModelMap();
        CartForm cartForm = new CartForm();

        when(cartService.getCart()).thenReturn(cart);
        when(cartFormMapper.convertToCartForm(cart)).thenReturn(cartForm);

        String response = cartPageController.updateCart(cartForm, bindingResult, model);

        assertEquals("redirect:/cart", response);
    }

    @Test
    void testUpdateCartWithOutOfStock() throws OutOfStockException {
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);

        Phone phone = new Phone();
        phone.setId(1L);

        CartItemForm cartItemForm = new CartItemForm(1L, 1);
        CartItem cartItem = new CartItem(phone, 1);

        Model model = new ExtendedModelMap();
        CartForm cartForm = new CartForm();
        List<CartItem> cartItems = new ArrayList<>();
        List<CartItemForm> cartDtoItems = new ArrayList<>();

        cartItems.add(cartItem);
        cartDtoItems.add(cartItemForm);

        cart.setCartItems(cartItems);
        cartForm.setItems(cartDtoItems);

        OutOfStockException e = new OutOfStockException(1L, 5);
        BindingResult newBindingResult = new BeanPropertyBindingResult(cartForm, WebConstants.CART_FORM_ATTR);
            newBindingResult.rejectValue("items[" + 0 + "].quantity",
                    WebConstants.OUT_OF_STOCK_ATTR,
                    String.format(WebConstants.ERROR_OUT_OF_STOCK_MESSAGE, e.getStock()));

        when(cartService.getCart()).thenReturn(cart);
        when(cartFormMapper.convertToCartForm(cart)).thenReturn(cartForm);
        when(cartFormMapper.convertToCartItems(cartForm)).thenReturn(cartItems);
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(e).when(cartService).update(cartItems);

        String response = cartPageController.updateCart(cartForm, bindingResult, model);

        assertEquals(BigDecimal.TEN, model.getAttribute(WebConstants.CART_COST_ATTR));
        assertEquals(1, model.getAttribute(WebConstants.CART_QUANTITY_ATTR));
        assertEquals(cart, model.getAttribute(WebConstants.CART_ATTR));
        assertEquals(cartForm, model.getAttribute(WebConstants.CART_FORM_ATTR));
        assertEquals(model.getAttribute(BindingResult.MODEL_KEY_PREFIX + WebConstants.CART_FORM_ATTR),
                newBindingResult);
        assertEquals("cart", response);
    }


    @Test
    void testDeleteCart() {
        Model model = new ExtendedModelMap();

        Long deletePhoneId = 1L;

        doNothing().when(cartService).remove(deletePhoneId);

        String response = cartPageController.deleteCart(deletePhoneId, model);
        verify(cartService).remove(deletePhoneId);

        assertEquals("redirect:/cart", response);
    }

    @Test
    void testDeleteCartWithException() {
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);

        Model model = new ExtendedModelMap();
        Long deletePhoneId = 1L;

        when(cartService.getCart()).thenReturn(cart);
        doThrow(ItemNotExistException.class).when(cartService).remove(deletePhoneId);

        String response = cartPageController.deleteCart(deletePhoneId, model);

        verify(cartService).remove(deletePhoneId);
        assertEquals(model.getAttribute(WebConstants.ERROR_MESSAGE), WebConstants.ERROR_NO_PHONE_WITH_ID_MESSAGE);

        assertEquals("cart", response);
    }


    @Test
    void testUpdateCartWithBindingResult() {
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);
        Model model = new ExtendedModelMap();
        CartForm cartForm = new CartForm();

        when(cartService.getCart()).thenReturn(cart);
        when(cartFormMapper.convertToCartForm(cart)).thenReturn(cartForm);
        when(bindingResult.hasErrors()).thenReturn(true);

        String response = cartPageController.updateCart(cartForm, bindingResult, model);

        verify(cartService).getCart();
        assertEquals(BigDecimal.TEN, model.getAttribute(WebConstants.CART_COST_ATTR));
        assertEquals(1, model.getAttribute(WebConstants.CART_QUANTITY_ATTR));
        assertEquals(cart, model.getAttribute(WebConstants.CART_ATTR));
        assertEquals(cartForm, model.getAttribute(WebConstants.CART_FORM_ATTR));
        assertEquals("cart", response);
    }

    @Test
    void testUpdateSuccess() throws OutOfStockException {
        Cart cart = new Cart();
        cart.setTotalCost(BigDecimal.TEN);
        cart.setTotalQuantity(1);

        Phone phone = new Phone();
        phone.setId(1L);

        CartItemForm cartItemForm = new CartItemForm(1L, 1);
        CartItem cartItem = new CartItem(phone, 1);

        Model model = new ExtendedModelMap();
        CartForm cartForm = new CartForm();
        List<CartItem> cartItems = new ArrayList<>();
        List<CartItemForm> cartDtoItems = new ArrayList<>();

        cartItems.add(cartItem);
        cartDtoItems.add(cartItemForm);

        cart.setCartItems(cartItems);
        cartForm.setItems(cartDtoItems);

        when(cartService.getCart()).thenReturn(cart);
        when(cartFormMapper.convertToCartForm(cart)).thenReturn(cartForm);
        when(cartFormMapper.convertToCartItems(cartForm)).thenReturn(cartItems);
        when(bindingResult.hasErrors()).thenReturn(false);

        String response = cartPageController.updateCart(cartForm, bindingResult, model);

        verify(cartService).getCart();
        verify(cartFormMapper).convertToCartItems(cartForm);
        verify(cartService).update(cartItems);
        assertEquals("redirect:/cart", response);
    }
}