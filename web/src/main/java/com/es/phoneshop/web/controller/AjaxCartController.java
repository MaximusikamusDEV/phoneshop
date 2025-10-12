package com.es.phoneshop.web.controller;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.order.OutOfStockException;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.dto.CartItemDto;
import com.es.core.cart.exceptions.CartValidationException;
import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.phoneshop.web.dto.AjaxCartResponseDto;
import com.es.phoneshop.web.dto.MiniCartDto;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<AjaxCartResponseDto> addPhone(@Valid @RequestBody CartItemDto cartItemDto,
                                                        BindingResult bindingResult) throws ItemNotExistException, OutOfStockException {
        if (bindingResult.hasErrors()) {
            String errorMsg = Optional.ofNullable(bindingResult.getFieldError())
                    .map(FieldError::getDefaultMessage)
                    .orElse(WebConstants.ERROR_UNKNOWN);
            throw new CartValidationException(errorMsg);
        }

        cartService.addPhone(cartItemDto.getPhoneId(), cartItemDto.getQuantity());
        AjaxCartResponseDto response = createAjaxCartResponse();

        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/miniCart")
    @ResponseBody
    public ResponseEntity<MiniCartDto> getMiniCart() {
        Cart cart = cartService.getCart();
        MiniCartDto miniCart = new MiniCartDto();
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
