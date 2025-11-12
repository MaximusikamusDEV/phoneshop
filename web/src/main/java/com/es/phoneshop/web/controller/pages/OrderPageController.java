package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.model.order.Order;
import com.es.core.order.OrderService;
import com.es.core.cart.exceptions.OutOfStockException;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.controller.forms.OrderForm;
import com.es.phoneshop.web.exceptions.EmptyCartException;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {
    @Resource
    private OrderService orderService;
    @Resource
    private CartService cartService;

    @GetMapping
    public String getOrder(Model model) {
        Cart cart = cartService.getCart();

        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new EmptyCartException(WebConstants.ERROR_EMPTY_CART);
        }

        Order order = orderService.createOrder(cart);

        model.addAttribute(WebConstants.ORDER_ATTR, order);
        model.addAttribute(WebConstants.ORDER_FORM_ATTR, new OrderForm());
        return "order";
    }

    @PostMapping
    public String placeOrder(@Valid @ModelAttribute(WebConstants.ORDER_FORM_ATTR) OrderForm orderForm,
                             BindingResult bindingResult,
                             Model model) {
        if (orderForm == null) {
            return "redirect:/order";
        }

        Order order = orderService.createOrder(cartService.getCart());
        populateOrderFormToOrder(order, orderForm);

        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            return "redirect:/order";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute(WebConstants.ORDER_ATTR, order);
            return "order";
        }

        try {
            orderService.placeOrder(order);
        } catch (OutOfStockException e) {
            bindingResult.reject(WebConstants.OUT_OF_STOCK_ERROR_CODE, WebConstants.OUT_OF_STOCK_INFO_MESSAGE);
            order = orderService.createOrder(cartService.getCart());

            model.addAttribute(WebConstants.ORDER_ATTR, order);

            return "order";
        }

        return "redirect:/orderOverview/" + order.getSecureId();
    }

    private void populateOrderFormToOrder(Order order, OrderForm orderForm) {
        order.setFirstName(orderForm.getFirstName());
        order.setLastName(orderForm.getLastName());
        order.setDeliveryAddress(orderForm.getDeliveryAddress());
        order.setContactPhoneNo(orderForm.getContactPhoneNo());
        order.setAdditionalInfo(orderForm.getAdditionalInfo());
    }
}
