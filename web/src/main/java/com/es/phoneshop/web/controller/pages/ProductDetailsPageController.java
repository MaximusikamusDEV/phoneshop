package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.services.PhoneService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping(value = "/productDetails")
public class ProductDetailsPageController {
    @Resource
    private PhoneService phoneService;
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.GET, value = "/{phoneId}")
    public String showProductDetails(@PathVariable("phoneId") Long id, Model model){
        model.addAttribute(WebConstants.CART_COST_ATTR, cartService.getCart().getTotalCost());
        model.addAttribute(WebConstants.CART_QUANTITY_ATTR, cartService.getCart().getTotalQuantity());
        model.addAttribute(WebConstants.PHONE_ATTR, phoneService.getPhoneById(id));

        return "productDetails";
    }
}
