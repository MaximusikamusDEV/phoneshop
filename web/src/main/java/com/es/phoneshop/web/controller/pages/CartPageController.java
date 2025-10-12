package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.cart.CartService;
import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.order.OutOfStockException;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.controller.mappers.CartDtoMapper;
import com.es.phoneshop.web.dto.CartDto;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;
    @Resource
    private CartDtoMapper cartDtoMapper;

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model) {
        Cart cart = cartService.getCart();
        CartDto cartDto = cartDtoMapper.convertToCartDto(cart);
        populateCartModel(model, cartDto);

        return "cart";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/updateCart")
    public String updateCart(@Valid @ModelAttribute(WebConstants.CART_DTO_ATTR) CartDto cartDto,
                             BindingResult bindingResult,
                             Model model,
                             @RequestParam(required = false) Long deletePhoneId) throws OutOfStockException {
        if (bindingResult.hasErrors()) {
            populateCartModel(model, cartDto);
            return "cart";
        }

        if(cartDto.getItems() == null || cartService.getCart().getCartItems() == null) {
            return "redirect:/cart";
        }

        if (deletePhoneId != null) {
            try {
                cartService.remove(deletePhoneId);
            } catch (ItemNotExistException e) {
                model.addAttribute(WebConstants.ERROR_MESSAGE, WebConstants.ERROR_NO_PHONE_WITH_ID_MESSAGE);
                populateCartModel(model, cartDto);

                return "cart";
            }

            return "redirect:/cart";
        }

        List<CartItem> cartItems = cartDtoMapper.convertToCartItems(cartDto);

        cartService.update(cartItems);

        return "redirect:/cart";
    }

    private void populateCartModel(Model model, CartDto cartDto) {
        Cart cart = cartService.getCart();
        model.addAttribute(WebConstants.CART_DTO_ATTR, cartDto);
        model.addAttribute(WebConstants.CART_ATTR, cart);
        model.addAttribute(WebConstants.CART_COST_ATTR, cart.getTotalCost());
        model.addAttribute(WebConstants.CART_QUANTITY_ATTR, cart.getTotalQuantity());
    }
}
