package com.es.phoneshop.web.controller.exceptionHandlers;

import ch.qos.logback.classic.Logger;
import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.order.OutOfStockException;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.controller.mappers.CartDtoMapper;
import com.es.phoneshop.web.controller.pages.CartPageController;
import com.es.phoneshop.web.dto.CartDto;
import com.es.phoneshop.web.dto.CartItemDto;
import jakarta.annotation.Resource;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;
import java.util.stream.IntStream;

@ControllerAdvice(assignableTypes = CartPageController.class)
public class CartPageExceptionHandler {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(CartPageExceptionHandler.class);
    @Resource
    private CartService cartService;
    @Resource
    private CartDtoMapper cartDtoMapper;

    @ExceptionHandler(OutOfStockException.class)
    public String handleOutOfStockException(OutOfStockException e, Model model) {
        Cart cart = cartService.getCart();

        CartDto cartDto = cartDtoMapper.convertToCartDto(cart);

        int itemIndex = findPhoneIndexInCartItems(cartDto.getItems(), e.getPhoneId());

        BindingResult bindingResult = new BeanPropertyBindingResult(cartDto, WebConstants.CART_DTO_ATTR);

        if (itemIndex != -1)
            bindingResult.rejectValue("items[" + itemIndex + "].quantity", "outOfStock", WebConstants.ERROR_OUT_OF_STOCK_MESSAGE);

        model.addAttribute(WebConstants.CART_DTO_ATTR, cartDto);
        model.addAttribute(WebConstants.CART_ATTR, cartService.getCart());
        model.addAttribute(WebConstants.CART_COST_ATTR, cartService.getCart().getTotalCost());
        model.addAttribute(WebConstants.CART_QUANTITY_ATTR, cartService.getCart().getTotalQuantity());
        model.addAttribute(BindingResult.MODEL_KEY_PREFIX + WebConstants.CART_DTO_ATTR, bindingResult);

        logger.error("{}", WebConstants.ERROR_OUT_OF_STOCK_MESSAGE);
        return "cart";
    }

    private int findPhoneIndexInCartItems(List<CartItemDto> cartItems, Long phoneId) {
        return IntStream.range(0, cartItems.size())
                .filter(i -> cartItems.get(i).getPhoneId().equals(phoneId))
                .findFirst()
                .orElse(-1);
    }
}
