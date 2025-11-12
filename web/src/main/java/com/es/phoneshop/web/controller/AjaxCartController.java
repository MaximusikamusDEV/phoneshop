package com.es.phoneshop.web.controller;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.model.exceptions.HighQuantityException;
import com.es.core.cart.exceptions.OutOfStockException;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.controller.forms.CartItemForm;
import com.es.core.cart.exceptions.CartValidationException;
import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.phoneshop.web.dto.AjaxCartResponseDto;
import com.es.phoneshop.web.controller.forms.MiniCart;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<AjaxCartResponseDto> addPhone(@Valid @RequestBody CartItemForm cartItemForm,
                                                        BindingResult bindingResult) throws ItemNotExistException, OutOfStockException, HighQuantityException {
        if (bindingResult.hasErrors()) {
            String errorMsg = Optional.ofNullable(bindingResult.getFieldError())
                    .map(FieldError::getDefaultMessage)
                    .orElse(WebConstants.ERROR_UNKNOWN);
            throw new CartValidationException(errorMsg);
        }

        cartService.addPhone(cartItemForm.getPhoneId(), cartItemForm.getQuantity());
        AjaxCartResponseDto response = createAjaxCartResponse();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/miniCart")
    @ResponseBody
    public ResponseEntity<MiniCart> getMiniCart() {
        Cart cart = cartService.getCart();
        MiniCart miniCart = new MiniCart();
        miniCart.setTotalCost(cart.getTotalCost());
        miniCart.setTotalQuantity(cart.getTotalQuantity());

        return ResponseEntity.ok().body(miniCart);
    }

    private AjaxCartResponseDto createAjaxCartResponse() throws ItemNotExistException {
        AjaxCartResponseDto response = new AjaxCartResponseDto();
        response.setStatus(WebConstants.SUCCESS_PARAM);
        response.setMessage(WebConstants.SUCCESS_ADD_MESSAGE);

        return response;
    }
}
