package com.es.phoneshop.web.controller.mappers;

import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.dto.CartDto;
import com.es.phoneshop.web.dto.CartItemDto;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartDtoMapper {
    @Resource
    private PhoneDao phoneDao;

    public List<CartItem> convertToCartItems(CartDto cartDto) {
        return cartDto.getItems().stream()
                .map(dtoItem -> {
                    Phone phone = phoneDao.get(dtoItem.getPhoneId())
                            .orElseThrow(() -> new ItemNotExistException(WebConstants.ERROR_NO_PHONE_WITH_ID_MESSAGE));
                    return new CartItem(phone, dtoItem.getQuantity());
                }).collect(Collectors.toList());
    }

    public CartDto convertToCartDto(Cart cart) {
        CartDto cartDto = new CartDto();
        List<CartItemDto> cartItems = cart.getCartItems().stream()
                .map(cartItem -> new CartItemDto(
                        cartItem.getPhone().getId(),
                        cartItem.getQuantity()
                ))
                .collect(Collectors.toList());
        cartDto.setItems(cartItems);

        return cartDto;
    }
}
