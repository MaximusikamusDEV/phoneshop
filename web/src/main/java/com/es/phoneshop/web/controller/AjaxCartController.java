package com.es.phoneshop.web.controller;

import com.es.core.cart.CartService;
import com.es.phoneshop.web.controller.Constants.WebConstants;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/ajaxCart")
@Validated
public class AjaxCartController {
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addPhone(@RequestParam @NotNull Long phoneId,
                                        @RequestParam @NotNull
                                        @Min(value = 1L,
                                                message = WebConstants.QUANTITY_VALID_MESSAGE)
                                        Long quantity) {
        Map<String, Object> response = new HashMap<>();

        try {
            cartService.addPhone(phoneId, quantity);
            response.put(WebConstants.STATUS_PARAM, WebConstants.SUCCESS_PARAM);
            response.put(WebConstants.MESSAGE_PARAM, WebConstants.SUCCESS_ADD_MESSAGE);
            response.put(WebConstants.CART_QUANTITY_ATTR, cartService.getCartQuantity());
            response.put(WebConstants.CART_COST_ATTR, cartService.getTotalCost());
        } catch (Exception e) {
            e.printStackTrace();
            response.put(WebConstants.MESSAGE_PARAM, WebConstants.ERROR_ADD_MESSAGE + e.getMessage());
            response.put(WebConstants.STATUS_PARAM, WebConstants.ERROR_ADD_MESSAGE);
        }

        return response;
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Map<String, Object> handleValidationExceptions (BindException ex){
        Map<String, Object> response = new HashMap<>();
        response.put(WebConstants.STATUS_PARAM, WebConstants.ERROR_ADD_MESSAGE);
        response.put(WebConstants.MESSAGE_PARAM, ex.getBindingResult().getFieldError().getDefaultMessage());
        return response;
    }
}
