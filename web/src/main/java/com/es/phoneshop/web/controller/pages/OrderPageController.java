package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.model.order.Order;
import com.es.core.order.OrderService;
import com.es.core.cart.exceptions.OutOfStockException;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.controller.facades.OrderFacade;
import com.es.phoneshop.web.controller.forms.OrderForm;
import com.es.phoneshop.web.exceptions.EmptyCartException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/order")
public class OrderPageController {
    @Resource
    private OrderService orderService;
    @Resource
    private CartService cartService;
    @Resource
    private OrderFacade orderFacade;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrder(Model model, HttpSession session) {
        Cart cart = cartService.getCart();

        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty())
            throw new EmptyCartException(WebConstants.ERROR_EMPTY_CART);

        Order order = orderService.createOrder(cart);
        session.setAttribute(WebConstants.ORDER_ATTR, order);
        populateOrderModel(order, model);
        model.addAttribute(WebConstants.ORDER_FORM_ATTR, new OrderForm());
        return "order";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String placeOrder(@Valid @ModelAttribute(WebConstants.ORDER_FORM_ATTR) OrderForm orderForm,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session) {

        Order order = (Order) session.getAttribute(WebConstants.ORDER_ATTR);

        if (order == null)
            return "redirect:/order";

        if (bindingResult.hasErrors()) {
            populateOrderModel(order, model);
            return "order";
        }

        if (order.getOrderItems() == null || order.getOrderItems().isEmpty())
            return "redirect:/order";

        populateOrderFormToOrder(order, orderForm);

        try {
            orderFacade.placeOrder(order);
            session.removeAttribute(WebConstants.ORDER_ATTR);
        } catch (OutOfStockException e) {
            bindingResult.reject("outOfStock", WebConstants.OUT_OF_STOCK_INFO);
            order = orderService.createOrder(cartService.getCart());
            session.setAttribute(WebConstants.ORDER_ATTR, order);
            populateOrderModel(order, model);
            return "order";
        }

        return "redirect:/orderOverview?orderId=" + order.getSecureId();
    }

    private void populateOrderModel(Order order, Model model) {
        model.addAttribute(WebConstants.ORDER_ATTR, order);
        model.addAttribute(WebConstants.CART_COST_ATTR, cartService.getCart().getTotalCost());
        model.addAttribute(WebConstants.CART_QUANTITY_ATTR, cartService.getCart().getTotalQuantity());
    }

    private void populateOrderFormToOrder(Order order, OrderForm orderForm) {
        order.setFirstName(orderForm.getFirstName());
        order.setLastName(orderForm.getLastName());
        order.setDeliveryAddress(orderForm.getDeliveryAddress());
        order.setContactPhoneNo(orderForm.getContactPhoneNo());
        order.setAdditionalInfo(orderForm.getAdditionalInfo());
    }
}
