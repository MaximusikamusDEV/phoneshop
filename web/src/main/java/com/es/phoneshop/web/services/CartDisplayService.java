package com.es.phoneshop.web.services;

import com.es.core.cart.CartService;
import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.phoneshop.web.dto.CartItemDto;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.dto.AjaxCartResponseDto;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class CartDisplayService {
    @Resource
    CartService cartService;

    public AjaxCartResponseDto addPhone(CartItemDto cartItemDto) throws ItemNotExistException {
        cartService.addPhone(cartItemDto.getPhoneId(), cartItemDto.getQuantity());
        AjaxCartResponseDto response = new AjaxCartResponseDto();
        response.setStatus(WebConstants.SUCCESS_PARAM);
        response.setMessage(WebConstants.SUCCESS_ADD_MESSAGE);
        response.setTotalQuantity(cartService.getCart().getTotalQuantity());
        response.setTotalCost(cartService.getCart().getTotalCost());
        return response;
    }

    public int getTotalQuantity() {
        return cartService.getCart().getTotalQuantity();
    }

    public BigDecimal getTotalCost() {
        return cartService.getCart().getTotalCost();
    }
}
