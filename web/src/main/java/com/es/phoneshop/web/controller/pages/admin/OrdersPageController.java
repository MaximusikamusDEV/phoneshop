package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;
import com.es.core.order.OrderService;
import com.es.core.stock.StockService;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.exceptions.EmptyOrderListException;
import com.es.phoneshop.web.exceptions.InvalidOrderIdException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/admin/orders")
public class OrdersPageController {
    @Resource
    private OrderService orderService;
    @Resource
    private StockService stockService;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrderListPage(Model model) {
        List<Order> orderList = orderService.getAllOrders()
                .orElseThrow(() -> new EmptyOrderListException(WebConstants.EMPTY_ORDER_LIST_MESSAGE));

        model.addAttribute(WebConstants.ORDER_LIST_ATTR, orderList);

        return "adminOrderList";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{orderId}")
    public String getOrderPage(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new InvalidOrderIdException(WebConstants.ERROR_INVALID_ORDER_ID));

        model.addAttribute(WebConstants.ORDER_ATTR, order);

        return "adminOrderOverview";
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{orderId}/status")
    public String changeOrderStatus(@PathVariable Long orderId,
                                    @RequestParam String newStatus,
                                    Model model) {
        OrderStatus status = OrderStatus.valueOfCode(newStatus);
        Order order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new InvalidOrderIdException(WebConstants.ERROR_INVALID_ORDER_ID));

        changeOrderStatus(order, status);

        model.addAttribute(WebConstants.ORDER_ATTR, order);

        return "redirect:/admin/orders/" + orderId;
    }

    private void changeOrderStatus(Order order, OrderStatus status) {
        orderService.updateOrderStatus(order, status);

        if(status.equals(OrderStatus.DELIVERED)) {
            order.getOrderItems().forEach(orderItem ->
                            stockService.confirmReserved(orderItem.getPhone(), orderItem.getQuantity())
                    );
        } else {
            order.getOrderItems().forEach(orderItem ->
                    stockService.returnReservedToStock(orderItem.getPhone(), orderItem.getQuantity())
            );
        }
    }
}
