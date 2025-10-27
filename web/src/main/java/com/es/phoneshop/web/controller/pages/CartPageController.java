package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.cart.CartService;
import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.order.OutOfStockException;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.controller.mappers.CartFormMapper;
import com.es.phoneshop.web.dto.CartForm;
import com.es.phoneshop.web.dto.CartItemForm;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;
    @Resource
    private CartFormMapper cartFormMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model) {
        Cart cart = cartService.getCart();
        CartForm cartForm = cartFormMapper.convertToCartDto(cart);
        populateCartModel(model, cartForm);

        return "cart";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/updateCart")
    public String updateCart(@Valid @ModelAttribute(WebConstants.CART_FORM_ATTR) CartForm cartForm,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            populateCartModel(model, cartForm);
            return "cart";
        }

        if (cartForm.getItems() == null || cartService.getCart().getCartItems() == null)
            return "redirect:/cart";


        List<CartItem> cartItems = cartFormMapper.convertToCartItems(cartForm);

        try {
            cartService.update(cartItems);
        } catch (OutOfStockException e) {
            handleOutOfStockException(cartForm, e, model);
            return "cart";
        }

        return "redirect:/cart";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{phoneId}")
    public String deleteCart(@PathVariable("phoneId") Long deletePhoneId, Model model) {
        try {
            cartService.remove(deletePhoneId);
        } catch (ItemNotExistException e) {
            model.addAttribute(WebConstants.ERROR_MESSAGE, WebConstants.ERROR_NO_PHONE_WITH_ID_MESSAGE);
            return "cart";
        }

        return "redirect:/cart";
    }

    private void populateCartModel(Model model, CartForm cartForm) {
        Cart cart = cartService.getCart();
        model.addAttribute(WebConstants.CART_FORM_ATTR, cartForm);
        model.addAttribute(WebConstants.CART_ATTR, cart);
        model.addAttribute(WebConstants.CART_COST_ATTR, cart.getTotalCost());
        model.addAttribute(WebConstants.CART_QUANTITY_ATTR, cart.getTotalQuantity());
    }

    private int findPhoneIndexInCartItems(List<CartItemForm> cartItems, Long phoneId) {
        return IntStream.range(0, cartItems.size())
                .filter(i -> cartItems.get(i).getPhoneId().equals(phoneId))
                .findFirst()
                .orElse(-1);
    }

    private void handleOutOfStockException(CartForm cartForm, OutOfStockException e, Model model) {
        int itemIndex = findPhoneIndexInCartItems(cartForm.getItems(), e.getPhoneId());

        BindingResult newBindingResult = new BeanPropertyBindingResult(cartForm, WebConstants.CART_FORM_ATTR);

        if (itemIndex != -1)
            newBindingResult.rejectValue("items[" + itemIndex + "].quantity",
                    WebConstants.OUT_OF_STOCK_ATTR,
                    String.format(WebConstants.ERROR_OUT_OF_STOCK_MESSAGE, e.getStock()));

        populateCartModel(model, cartForm);
        model.addAttribute(BindingResult.MODEL_KEY_PREFIX + WebConstants.CART_FORM_ATTR, newBindingResult);
    }
}
