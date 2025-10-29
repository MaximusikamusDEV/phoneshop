package com.es.phoneshop.web.controller.pages;

import com.es.core.model.order.Order;
import com.es.core.order.OrderService;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.exceptions.InvalidOrderIdException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/orderOverview")
public class OrderOverviewPageController {
    @Resource
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET, value = "/{orderId}")
    public String showOrderOverviewPage(@PathVariable String orderId, Model model) {
        Order order = orderService.getOrderBySecureId(orderId)
                .orElseThrow(() -> new InvalidOrderIdException(WebConstants.ERROR_INVALID_ORDER_ID));

        model.addAttribute(WebConstants.ORDER_ATTR, order);

        return "orderOverview";
    }
}
