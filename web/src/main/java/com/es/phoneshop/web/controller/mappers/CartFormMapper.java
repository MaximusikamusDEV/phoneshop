package com.es.phoneshop.web.controller.mappers;

import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.controller.forms.CartForm;
import com.es.phoneshop.web.controller.forms.CartItemForm;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartFormMapper {
    @Resource
    private PhoneDao phoneDao;

    public List<CartItem> convertToCartItems(CartForm cartForm) {
        return cartForm.getItems().stream()
                .map(formItem -> {
                    Phone phone = phoneDao.get(formItem.getPhoneId())
                            .orElseThrow(() -> new ItemNotExistException(WebConstants.ERROR_NO_PHONE_WITH_ID_MESSAGE));
                    return new CartItem(phone, formItem.getQuantity());
                }).collect(Collectors.toList());
    }

    public CartForm convertToCartForm(Cart cart) {
        CartForm cartForm = new CartForm();
        List<CartItemForm> cartItems = cart.getCartItems().stream()
                .map(cartItem -> new CartItemForm(
                        cartItem.getPhone().getId(),
                        cartItem.getQuantity()
                ))
                .collect(Collectors.toList());

        cartForm.setItems(cartItems);

        return cartForm;
    }
}
